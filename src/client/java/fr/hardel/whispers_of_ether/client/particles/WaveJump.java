package fr.hardel.whispers_of_ether.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.joml.Quaternionf;

public class WaveJump extends TextureSheetParticle {
    private static final Quaternionf HORIZONTAL = new Quaternionf().rotateX((float) -Math.PI / 2);

    private final int ringIndex;
    private final SpriteSet sprites;
    private float rotationAngle;

    protected WaveJump(ClientLevel level, double x, double y, double z, SpriteSet sprites, int ringIndex) {
        super(level, x, y, z);
        this.ringIndex = ringIndex;
        this.sprites = sprites;
        this.lifetime = 15;
        this.gravity = 0;
        this.friction = 1.0F;
        this.quadSize = 0.1f;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.setSprite(sprites.get(0, 1));
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
        this.setSprite(sprites.get(age, lifetime));
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Quaternionf rotation = new Quaternionf(HORIZONTAL)
                .rotateZ(this.rotationAngle + partialTick * 0.1f);
        this.renderRotatedQuad(buffer, camera, rotation, partialTick);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Factory(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                double xSpeed, double ySpeed, double zSpeed) {
            int ringIndex = (int) Math.round(Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed + zSpeed * zSpeed));
            if (ringIndex < 0 || ringIndex > 2) {
                ringIndex = 0;
            }
            return new WaveJump(level, x, y, z, sprites, ringIndex);
        }
    }
}
