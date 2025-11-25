package fr.hardel.whispers_of_ether.spell;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Optional;

public class SpellActionExecutor {
    public static void execute(SpellAction spellAction, ServerLevel world, Entity caster) {
        if (spellAction.condition().isPresent()) {
            LootParams lootWorldContext = new LootParams.Builder(world).withParameter(LootContextParams.THIS_ENTITY, caster).withParameter(LootContextParams.ORIGIN, caster.position()).create(LootContextParamSets.COMMAND);
            LootContext lootContext = new LootContext.Builder(lootWorldContext).create(Optional.empty());

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