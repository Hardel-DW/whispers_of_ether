package fr.hardel.whispers_of_ether.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.spell.timeline.OrganizationTimeline;
import fr.hardel.whispers_of_ether.spell.timeline.TimelineAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Optional;

public record Spell(ResourceLocation icon, String name, Optional<Integer> cooldown,
        Optional<LootItemCondition> condition, List<TimelineAction> timelines, OrganizationTimeline organization,
        List<SpellAction> passive) {

    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("icon").forGetter(Spell::icon),
            Codec.STRING.fieldOf("name").forGetter(Spell::name),
            Codec.INT.optionalFieldOf("cooldown").forGetter(Spell::cooldown),
            LootItemCondition.DIRECT_CODEC.optionalFieldOf("condition").forGetter(Spell::condition),
            TimelineAction.CODEC.listOf().fieldOf("timelines").forGetter(Spell::timelines),
            OrganizationTimeline.CODEC.fieldOf("organization").forGetter(Spell::organization),
            SpellAction.CODEC.listOf().optionalFieldOf("passive", List.of()).forGetter(Spell::passive))
            .apply(instance, Spell::new));

    public ResourceLocation getTextureId() {
        return ResourceLocation.fromNamespaceAndPath(icon.getNamespace(), "textures/spell/" + icon.getPath() + ".png");
    }

    public boolean canCast(Player caster, ResourceLocation spellId) {
        var spellComponent = ModComponents.PLAYER_SPELL.get(caster);
        if (spellComponent.isOnCooldown(spellId)) {
            return false;
        }

        if (condition.isEmpty() || !(caster.level() instanceof ServerLevel)) {
            return true;
        }

        if (!(caster.level() instanceof ServerLevel serverWorld))
            return true;

        LootParams lootWorldContext = new LootParams.Builder(serverWorld)
                .withParameter(LootContextParams.THIS_ENTITY, caster)
                .withParameter(LootContextParams.ORIGIN, caster.position())
                .create(LootContextParamSets.COMMAND);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).create(Optional.empty());
        return condition.get().test(lootContext);
    }

    public void cast(Player caster, ResourceLocation spellId) {
        if (!canCast(caster, spellId))
            return;

        if (!(caster.level() instanceof ServerLevel serverWorld))
            return;

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