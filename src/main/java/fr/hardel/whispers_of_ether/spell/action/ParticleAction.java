package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public record ParticleAction(ResourceLocation particle, Optional<Position> offset, Optional<Position> delta, int count,
        double speed, boolean force) implements Action {

    public static final MapCodec<ParticleAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("particle").forGetter(ParticleAction::particle),
            Position.CODEC.optionalFieldOf("offset").forGetter(ParticleAction::offset),
            Position.CODEC.optionalFieldOf("delta").forGetter(ParticleAction::delta),
            Codec.INT.optionalFieldOf("count", 1).forGetter(ParticleAction::count),
            Codec.DOUBLE.optionalFieldOf("speed", 0.0).forGetter(ParticleAction::speed),
            Codec.BOOL.optionalFieldOf("force", false).forGetter(ParticleAction::force))
            .apply(instance, ParticleAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.PARTICLE;
    }

    @Override
    public void execute(ServerLevel world, Entity caster, List<Entity> targets) {
        ParticleType<?> particleType = BuiltInRegistries.PARTICLE_TYPE.getValue(particle);
        if (particleType == null) {
            return;
        }

        ParticleOptions particleEffect = (ParticleOptions) particleType;

        for (Entity target : targets) {
            Vec3 targetPos = target.position();

            double x = targetPos.x;
            double y = targetPos.y;
            double z = targetPos.z;

            if (offset.isPresent()) {
                Position pos = offset.get();
                x += pos.x();
                y += pos.y();
                z += pos.z();
            }

            double deltaX = 0.0;
            double deltaY = 0.0;
            double deltaZ = 0.0;

            if (delta.isPresent()) {
                Position deltaPos = delta.get();
                deltaX = deltaPos.x();
                deltaY = deltaPos.y();
                deltaZ = deltaPos.z();
            }

            world.sendParticles(
                    particleEffect,
                    force,
                    false,
                    x, y, z,
                    count,
                    deltaX, deltaY, deltaZ,
                    speed);
        }
    }
}