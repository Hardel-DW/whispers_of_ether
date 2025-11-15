package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record RelativePosition(Position offset) implements PositionTarget {

    public static final MapCodec<RelativePosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.fieldOf("offset").forGetter(RelativePosition::offset))
            .apply(instance, RelativePosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.RELATIVE;
    }

    @Override
    public Vec3 toVec3d(Entity caster) {
        return caster.position().add(offset.x(), offset.y(), offset.z());
    }
}