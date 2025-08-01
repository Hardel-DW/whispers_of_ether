package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public record RelativePosition(Position offset) implements PositionTarget {

    public static final MapCodec<RelativePosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RecordCodecBuilder.mapCodec(posInstance -> posInstance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(pos -> ((Position) pos).x()),
                    Codec.FLOAT.fieldOf("y").forGetter(pos -> ((Position) pos).y()),
                    Codec.FLOAT.fieldOf("z").forGetter(pos -> ((Position) pos).z())).apply(posInstance, Position::new))
                    .fieldOf("offset").forGetter(RelativePosition::offset))
            .apply(instance, RelativePosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.RELATIVE;
    }

    @Override
    public Vec3d toVec3d(Entity caster) {
        return caster.getPos().add(offset.x(), offset.y(), offset.z());
    }
}