package fr.hardel.whispers_of_ether.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.action.Action;
import fr.hardel.whispers_of_ether.spell.target.SelfTarget;
import fr.hardel.whispers_of_ether.spell.target.Target;
import net.minecraft.loot.condition.LootCondition;

import java.util.Optional;

public record SpellAction(Action action, Optional<LootCondition> condition, Target target) {
    
    public static final Codec<SpellAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Action.CODEC.fieldOf("action").forGetter(SpellAction::action),
        LootCondition.CODEC.optionalFieldOf("condition").forGetter(SpellAction::condition),
        Target.CODEC.optionalFieldOf("target", new SelfTarget()).forGetter(SpellAction::target)
    ).apply(instance, SpellAction::new));
}