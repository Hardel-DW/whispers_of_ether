package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record LocalPosition(Position offset) implements PositionTarget {

    public static final MapCodec<LocalPosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.fieldOf("offset").forGetter(LocalPosition::offset))
            .apply(instance, LocalPosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.LOCAL;
    }

    @Override
    public Vec3 toVec3d(Entity caster) {
        Vec3 forward = caster.getLookAngle();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(forward).normalize();

        return caster.position()
                .add(forward.scale(offset.z()))
                .add(right.scale(offset.x()))
                .add(up.scale(offset.y()));
    }
}