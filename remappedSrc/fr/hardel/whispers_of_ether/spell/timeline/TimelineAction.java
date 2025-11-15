package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.SpellAction;
import java.util.List;
import java.util.Optional;
import net.minecraft.loot.condition.LootCondition;

public record TimelineAction(String id, List<SpellAction> actions, int duration, Optional<LootCondition> condition, Optional<Integer> waitTime) {
    
    public static final Codec<TimelineAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("id").forGetter(TimelineAction::id),
        SpellAction.CODEC.listOf().fieldOf("actions").forGetter(TimelineAction::actions),
        Codec.INT.fieldOf("duration").forGetter(TimelineAction::duration),
        LootCondition.CODEC.optionalFieldOf("condition").forGetter(TimelineAction::condition),
        Codec.INT.optionalFieldOf("wait").forGetter(TimelineAction::waitTime)
    ).apply(instance, TimelineAction::new));
}