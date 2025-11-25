package fr.hardel.whispers_of_ether.runic_attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeData(
    ResourceLocation attribute,
    AttributeModifier.Operation operation,
    double weight,
    double maxValue,
    Range onPass
) {
    public record Range(double min, double max) {
        public static final Codec<Range> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("min").forGetter(Range::min),
            Codec.DOUBLE.fieldOf("max").forGetter(Range::max)
        ).apply(instance, Range::new));
    }

    public static final Codec<AttributeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("attribute").forGetter(AttributeData::attribute),
        AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeData::operation),
        Codec.DOUBLE.fieldOf("weight").forGetter(AttributeData::weight),
        Codec.DOUBLE.fieldOf("max_value").forGetter(AttributeData::maxValue),
        Range.CODEC.fieldOf("on_pass").forGetter(AttributeData::onPass)
    ).apply(instance, AttributeData::new));
}
