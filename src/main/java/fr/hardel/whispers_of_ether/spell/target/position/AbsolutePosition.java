package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public record AbsolutePosition(Position position) implements PositionTarget {

    public static final MapCodec<AbsolutePosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RecordCodecBuilder.mapCodec(posInstance -> posInstance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(pos -> ((Position) pos).x()),
                    Codec.FLOAT.fieldOf("y").forGetter(pos -> ((Position) pos).y()),
                    Codec.FLOAT.fieldOf("z").forGetter(pos -> ((Position) pos).z())).apply(posInstance, Position::new))
                    .fieldOf("position").forGetter(AbsolutePosition::position))
            .apply(instance, AbsolutePosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.ABSOLUTE;
    }

    @Override
    public Vec3d toVec3d(Entity caster) {
        return new Vec3d(position.x(), position.y(), position.z());
    }
}