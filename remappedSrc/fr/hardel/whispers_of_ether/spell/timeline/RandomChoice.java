package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record RandomChoice(String timelineId, int weight) {
    public static final Codec<RandomChoice> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("timeline_id").forGetter(RandomChoice::timelineId),
        Codec.INT.fieldOf("weight").forGetter(RandomChoice::weight)
    ).apply(instance, RandomChoice::new));
}