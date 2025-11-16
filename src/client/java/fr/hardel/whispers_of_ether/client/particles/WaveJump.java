package fr.hardel.whispers_of_ether.client.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.state.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class WaveJump extends SingleQuadParticle {
    private final int ringIndex;
    private float rotationAngle = 0.0f;

    protected WaveJump(ClientLevel level, double x, double y, double z, SpriteSet sprites, int ringIndex) {
        super(level, x, y, z, sprites.first());
        this.ringIndex = ringIndex;
        this.lifetime = 15;
        this.gravity = 0;
        this.friction = 1.0F;
        this.quadSize = 0.1f;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = (float) this.age / this.lifetime;
        float smoothProgress = progress * progress * (3.0f - 2.0f * progress);
        float intensityMultiplier = ringIndex * 0.5f;
        this.quadSize = (0.3f + smoothProgress * 2.0f) * (0.3f + intensityMultiplier);
        this.alpha = 1.0f - smoothProgress;
        this.rotationAngle += 0.1f;
    }

    @Override
    protected SingleQuadParticle.@NotNull Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    @Override
    public void extract(QuadParticleRenderState state, Camera camera, float partialTick) {
        float halfAngle = (float) (Math.PI / 4);
        double factor = Math.sqrt(2) / 2;
        double sf = Math.sin(halfAngle) * factor;
        double cf = Math.cos(halfAngle) * factor;
        Quaternionf baseRotation = new Quaternionf((float) -cf, (float) sf, (float) sf, (float) cf);

        float interpolatedRotation = this.rotationAngle + partialTick * 0.1f;
        Quaternionf xRotation = new Quaternionf().rotateZ(interpolatedRotation);
        Quaternionf finalRotation = baseRotation.mul(xRotation);

        this.extractRotatedQuad(state, camera, finalRotation, partialTick);
    }

    public record Factory(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            int ringIndex = (int) Math.round(Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed));
            if (ringIndex < 0 || ringIndex > 2) {
                ringIndex = 0;
            }
            return new WaveJump(level, x, y, z, sprites, ringIndex);
        }
    }
}
