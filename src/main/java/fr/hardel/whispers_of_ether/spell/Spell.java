package fr.hardel.whispers_of_ether.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.spell.timeline.OrganizationTimeline;
import fr.hardel.whispers_of_ether.spell.timeline.TimelineAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import java.util.List;
import java.util.Optional;

public record Spell(Identifier icon, String name, Optional<Integer> cooldown,
        Optional<LootCondition> condition, List<TimelineAction> timelines, OrganizationTimeline organization,
        List<SpellAction> passive) {

    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("icon").forGetter(Spell::icon),
            Codec.STRING.fieldOf("name").forGetter(Spell::name),
            Codec.INT.optionalFieldOf("cooldown").forGetter(Spell::cooldown),
            LootCondition.CODEC.optionalFieldOf("condition").forGetter(Spell::condition),
            TimelineAction.CODEC.listOf().fieldOf("timelines").forGetter(Spell::timelines),
            OrganizationTimeline.CODEC.fieldOf("organization").forGetter(Spell::organization),
            SpellAction.CODEC.listOf().optionalFieldOf("passive", List.of()).forGetter(Spell::passive))
            .apply(instance, Spell::new));

    public Identifier getTextureId() {
        return Identifier.of(icon.getNamespace(), "textures/spell/" + icon.getPath() + ".png");
    }

    public boolean canCast(PlayerEntity caster, Identifier spellId) {
        // Vérifier le cooldown d'abord
        var spellComponent = ModComponents.PLAYER_SPELL.get(caster);
        if (spellComponent.isOnCooldown(spellId)) {
            return false;
        }

        if (condition.isEmpty() || !(caster.getWorld() instanceof ServerWorld)) {
            return true;
        }

        if (!(caster.getWorld() instanceof ServerWorld serverWorld))
            return true;

        LootWorldContext lootWorldContext = new LootWorldContext.Builder(serverWorld)
                .add(LootContextParameters.THIS_ENTITY, caster)
                .add(LootContextParameters.ORIGIN, caster.getPos())
                .build(LootContextTypes.COMMAND);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());
        return condition.get().test(lootContext);
    }

    public void cast(PlayerEntity caster, Identifier spellId) {
        if (!canCast(caster, spellId))
            return;

        if (!(caster.getWorld() instanceof ServerWorld serverWorld))
            return;

        // Déclencher le cooldown après un cast réussi
        if (cooldown.isPresent() && cooldown.get() > 0) {
            var spellComponent = ModComponents.PLAYER_SPELL.get(caster);
            long endTime = System.currentTimeMillis() + (cooldown.get() * 1000L);
            spellComponent.setCooldown(spellId, endTime);
        }

        for (SpellAction action : passive) {
            SpellActionExecutor.execute(action, serverWorld, caster);
        }

        if (!timelines.isEmpty()) {
            organization.execute(timelines, serverWorld, caster);
        }
    }
}