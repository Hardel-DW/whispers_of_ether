package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;

public record RandomBoxOffset(Position boxSize) implements LoopOffset {

    public static final MapCodec<RandomBoxOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RecordCodecBuilder.mapCodec(posInstance -> posInstance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(pos -> ((Position) pos).x()),
                    Codec.FLOAT.fieldOf("y").forGetter(pos -> ((Position) pos).y()),
                    Codec.FLOAT.fieldOf("z").forGetter(pos -> ((Position) pos).z())).apply(posInstance, Position::new))
                    .fieldOf("box_size").forGetter(RandomBoxOffset::boxSize))
            .apply(instance, RandomBoxOffset::new));

    @Override
    public LoopOffsetType<?> getType() {
        return LoopOffsetType.RANDOM_BOX;
    }

    @Override
    public Position calculateOffset(int iteration, Entity caster) {
        Random random = caster.getWorld().getRandom();

        float offsetX = (random.nextFloat() - 0.5f) * boxSize.x();
        float offsetY = (random.nextFloat() - 0.5f) * boxSize.y();
        float offsetZ = (random.nextFloat() - 0.5f) * boxSize.z();

        return new Position(offsetX, offsetY, offsetZ);
    }
}