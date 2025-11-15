package fr.hardel.whispers_of_ether.spell;

import java.util.List;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;

public class SpellActionExecutor {
    public static void execute(SpellAction spellAction, ServerWorld world, Entity caster) {
        if (spellAction.condition().isPresent()) {
            LootWorldContext lootWorldContext = new LootWorldContext.Builder(world)
                    .add(LootContextParameters.THIS_ENTITY, caster)
                    .add(LootContextParameters.ORIGIN, caster.getEntityPos())
                    .build(LootContextTypes.COMMAND);
            LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());

            if (!spellAction.condition().get().test(lootContext)) {
                return;
            }
        }

        List<Entity> targets = spellAction.target().resolve(world, caster);
        if (targets.isEmpty()) {
            return;
        }

        spellAction.action().execute(world, caster, targets);
    }
}