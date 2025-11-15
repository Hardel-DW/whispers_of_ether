package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.random.Random;

public record RandomValueOffset(OffsetAxis axis, float minValue, float maxValue, OffsetType offsetType) implements LoopOffset {

    public static final MapCodec<RandomValueOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.xmap(OffsetAxis::valueOf, OffsetAxis::name).fieldOf("axis").forGetter(RandomValueOffset::axis),
            Codec.FLOAT.fieldOf("min_value").forGetter(RandomValueOffset::minValue),
            Codec.FLOAT.fieldOf("max_value").forGetter(RandomValueOffset::maxValue),
            Codec.STRING.xmap(OffsetType::valueOf, OffsetType::name).fieldOf("offset_type").forGetter(RandomValueOffset::offsetType))
            .apply(instance, RandomValueOffset::new));

    @Override
    public LoopOffsetType<?> getType() {
        return LoopOffsetType.RANDOM_VALUE;
    }

    @Override
    public Position calculateOffset(int iteration, Entity caster) {
        Random random = caster.getEntityWorld().getRandom();
        float randomValue = minValue + random.nextFloat() * (maxValue - minValue);
        
        return switch (axis) {
            case X -> new Position(randomValue, 0, 0);
            case Y -> new Position(0, randomValue, 0);
            case Z -> new Position(0, 0, randomValue);
        };
    }

    public enum OffsetAxis {
        X, Y, Z
    }

    public enum OffsetType {
        RELATIVE, LOCAL, ABSOLUTE
    }
}