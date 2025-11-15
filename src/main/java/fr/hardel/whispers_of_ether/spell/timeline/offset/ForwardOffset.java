package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record ForwardOffset(float distancePerIteration) implements LoopOffset {

    public static final MapCodec<ForwardOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("distance_per_iteration", 1.0f).forGetter(ForwardOffset::distancePerIteration))
            .apply(instance, ForwardOffset::new));

    @Override
    public LoopOffsetType<?> getType() {
        return LoopOffsetType.FORWARD;
    }

    @Override
    public Position calculateOffset(int iteration, Entity caster) {
        Vec3 lookDirection = caster.getLookAngle();
        float distance = distancePerIteration * iteration;

        return new Position(
                (float) (lookDirection.x * distance),
                0.0f,
                (float) (lookDirection.z * distance));
    }
}