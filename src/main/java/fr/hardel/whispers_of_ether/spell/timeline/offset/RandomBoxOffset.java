package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;

public record RandomBoxOffset(Position boxSize) implements LoopOffset {

    public static final MapCodec<RandomBoxOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.fieldOf("box_size").forGetter(RandomBoxOffset::boxSize))
            .apply(instance, RandomBoxOffset::new));

    @Override
    public LoopOffsetType<?> getType() {
        return LoopOffsetType.RANDOM_BOX;
    }

    @Override
    public Position calculateOffset(int iteration, Entity caster) {
        RandomSource random = caster.level().getRandom();

        float offsetX = (random.nextFloat() - 0.5f) * boxSize.x();
        float offsetY = (random.nextFloat() - 0.5f) * boxSize.y();
        float offsetZ = (random.nextFloat() - 0.5f) * boxSize.z();

        return new Position(offsetX, offsetY, offsetZ);
    }
}