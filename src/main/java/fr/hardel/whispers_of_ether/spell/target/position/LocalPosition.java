package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public record LocalPosition(Position offset) implements PositionTarget {

    public static final MapCodec<LocalPosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.fieldOf("offset").forGetter(LocalPosition::offset))
            .apply(instance, LocalPosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.LOCAL;
    }

    @Override
    public Vec3d toVec3d(Entity caster) {
        Vec3d forward = caster.getRotationVector();
        Vec3d right = forward.crossProduct(new Vec3d(0, 1, 0)).normalize();
        Vec3d up = right.crossProduct(forward).normalize();

        return caster.getPos()
                .add(forward.multiply(offset.z()))
                .add(right.multiply(offset.x()))
                .add(up.multiply(offset.y()));
    }
}