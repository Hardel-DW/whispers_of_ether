package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record AbsolutePosition(Position position) implements PositionTarget {

    public static final MapCodec<AbsolutePosition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.fieldOf("position").forGetter(AbsolutePosition::position))
            .apply(instance, AbsolutePosition::new));

    @Override
    public PositionTargetType<?> getType() {
        return PositionTargetType.ABSOLUTE;
    }

    @Override
    public Vec3 toVec3d(Entity caster) {
        return new Vec3(position.x(), position.y(), position.z());
    }
}