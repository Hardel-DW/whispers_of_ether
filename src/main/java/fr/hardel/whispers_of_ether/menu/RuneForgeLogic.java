package fr.hardel.whispers_of_ether.menu;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import fr.hardel.whispers_of_ether.component.item.WellComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RuneForgeLogic {
    private static final double[] BASE_PROBABILITIES = {0.30, 0.45, 0.60, 0.75, 0.90};
    private static final double MAX_STAT_WEIGHT = 101.0;
    private static final Random RANDOM = new Random();

    public enum Outcome {
        CRITICAL_SUCCESS,
        NEUTRAL_SUCCESS,
        CRITICAL_FAILURE,
        BLOCKED
    }

    public record ForgeResult(Outcome outcome, ItemStack resultStack, String message) {}

    public static ForgeResult applyRune(ItemStack runeStack, ItemStack equipmentStack) {
        RuneComponent rune = runeStack.get(ModItemComponent.RUNES);
        if (rune == null) {
            return new ForgeResult(Outcome.BLOCKED, equipmentStack, "forge.whispers_of_ether.no_rune");
        }

        WellComponent well = equipmentStack.getOrDefault(ModItemComponent.WELL, WellComponent.EMPTY);
        System.out.println("[LOGIC] Well: " + well.currentWell() + "/" + well.maxWell());

        double currentValue = getAttributeValue(equipmentStack, rune.attributeId());
        double maxValue = getMaxAttributeValue(rune.attributeId());
        System.out.println("[LOGIC] Attribute " + rune.attributeId() + ": current=" + currentValue + ", max=" + maxValue);

        if (currentValue >= maxValue) {
            return new ForgeResult(Outcome.BLOCKED, equipmentStack, "forge.whispers_of_ether.max_stat");
        }

        double wellConsumed = calculateWellConsumption(rune.weight(), currentValue, maxValue);
        System.out.println("[LOGIC] Well consumed: " + wellConsumed);

        if (well.currentWell() < wellConsumed) {
            return new ForgeResult(Outcome.BLOCKED, equipmentStack, "forge.whispers_of_ether.insufficient_well");
        }

        WellComponent newWell = well.consume(wellConsumed);
        double totalItemWeight = calculateTotalWeight(equipmentStack);

        Probabilities prob = calculateProbabilities(rune.tier(), currentValue, maxValue, newWell.getWellFactor());
        Outcome outcome = determineOutcome(prob);

        ItemStack result = applyOutcome(equipmentStack, rune, outcome, totalItemWeight);
        result.set(ModItemComponent.WELL, newWell);

        String message = switch (outcome) {
            case CRITICAL_SUCCESS -> "forge.whispers_of_ether.critical_success";
            case NEUTRAL_SUCCESS -> "forge.whispers_of_ether.neutral_success";
            case CRITICAL_FAILURE -> "forge.whispers_of_ether.critical_failure";
            case BLOCKED -> "forge.whispers_of_ether.blocked";
        };

        return new ForgeResult(outcome, result, message);
    }

    private static double calculateWellConsumption(double runeWeight, double currentValue, double maxValue) {
        double currentStatLevel = maxValue > 0 ? currentValue / maxValue : 0;
        return runeWeight * currentStatLevel;
    }

    private static Probabilities calculateProbabilities(int tier, double currentValue, double maxValue, double wellFactor) {
        int tierIndex = tier - 1;
        if (tierIndex < 0 || tierIndex >= BASE_PROBABILITIES.length) {
            tierIndex = 0;
        }

        double baseProb = BASE_PROBABILITIES[tierIndex];
        double penaltyMax = maxValue > 0 ? (currentValue / maxValue) * 0.5 : 0;

        double penaltyWell = wellFactor < 0.2 ? 0.3 : (wellFactor < 0.5 ? 0.1 : 0);

        double critSuccess = baseProb * (1 - penaltyMax) * 0.15;
        double neutralSuccess = baseProb * (1 - penaltyMax) * (1 - penaltyWell);
        double critFailure = 1 - critSuccess - neutralSuccess;

        return new Probabilities(
            Math.max(0, Math.min(1, critSuccess)),
            Math.max(0, Math.min(1, neutralSuccess)),
            Math.max(0, Math.min(1, critFailure))
        );
    }

    private static Outcome determineOutcome(Probabilities prob) {
        double roll = RANDOM.nextDouble();

        if (roll < prob.criticalSuccess) {
            return Outcome.CRITICAL_SUCCESS;
        }
        if (roll < prob.criticalSuccess + prob.neutralSuccess) {
            return Outcome.NEUTRAL_SUCCESS;
        }
        return Outcome.CRITICAL_FAILURE;
    }

    private static ItemStack applyOutcome(ItemStack equipment, RuneComponent rune, Outcome outcome, double totalWeight) {
        ItemStack result = equipment.copy();

        double statGain = switch (outcome) {
            case CRITICAL_SUCCESS -> rune.value() * 2;
            case NEUTRAL_SUCCESS -> rune.value();
            case CRITICAL_FAILURE -> 0;
            case BLOCKED -> 0;
        };

        if (statGain > 0) {
            addAttributeModifier(result, rune.attributeId(), rune.operation(), statGain);
        }

        if (outcome == Outcome.NEUTRAL_SUCCESS || outcome == Outcome.CRITICAL_FAILURE) {
            double lossMultiplier = outcome == Outcome.NEUTRAL_SUCCESS ? 0.1 : 0.2;
            applyStatLoss(result, rune.attributeId(), rune.weight(), totalWeight, lossMultiplier);
        }

        return result;
    }

    private static void addAttributeModifier(ItemStack stack, ResourceLocation attributeId, AttributeModifier.Operation operation, double value) {
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>(modifiers.modifiers());
        Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.get(
            ResourceKey.create(Registries.ATTRIBUTE, attributeId)
        );

        if (attributeHolder.isEmpty()) return;
        entries.add(new ItemAttributeModifiers.Entry(
            attributeHolder.get(),
            new AttributeModifier(attributeId, value, operation),
            EquipmentSlotGroup.ANY
        ));

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(entries));
    }

    private static void applyStatLoss(ItemStack stack, ResourceLocation excludeAttribute, double runeWeight, double totalWeight, double multiplier) {
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        if (totalWeight <= 0) {
            return;
        }

        double lossFactor = (runeWeight / totalWeight) * multiplier;
        List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> holder = entry.attribute();
            ResourceLocation attrId = BuiltInRegistries.ATTRIBUTE.getKey(holder.value());

            if (!attrId.equals(excludeAttribute)) {
                double newValue = entry.modifier().amount() * (1 - lossFactor);
                newEntries.add(new ItemAttributeModifiers.Entry(
                    holder,
                    new AttributeModifier(entry.modifier().id(), Math.max(0, newValue), entry.modifier().operation()),
                    entry.slot()
                ));
            } else {
                newEntries.add(entry);
            }
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(newEntries));
    }

    private static double getAttributeValue(ItemStack stack, ResourceLocation attributeId) {
        ItemAttributeModifiers modifiers = stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        return modifiers.modifiers().stream()
            .filter(entry -> {
                ResourceLocation id = BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value());
                return id.equals(attributeId);
            })
            .mapToDouble(entry -> entry.modifier().amount())
            .sum();
    }

    private static double calculateTotalWeight(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        return modifiers.modifiers().stream()
            .mapToDouble(entry -> {
                double value = entry.modifier().amount();
                ResourceLocation attrId = BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value());
                double weight = getAttributeWeight(attrId);
                return Math.abs(value) * weight;
            })
            .sum();
    }

    private static double getAttributeWeight(ResourceLocation attributeId) {
        return switch (attributeId.toString()) {
            case "minecraft:generic.armor" -> 5.0;
            case "minecraft:generic.attack_damage" -> 20.0;
            case "minecraft:generic.attack_speed" -> 10.0;
            case "minecraft:generic.attack_knockback" -> 10.0;
            case "minecraft:generic.max_health" -> 1.0;
            case "minecraft:generic.movement_speed" -> 200.0;
            case "minecraft:generic.knockback_resistance" -> 20.0;
            case "whispers_of_ether:crit_rate" -> 10.0;
            case "whispers_of_ether:crit_damage" -> 10.0;
            case "whispers_of_ether:omnivampirism" -> 10.0;
            case "whispers_of_ether:omnivampirism_rate" -> 10.0;
            case "whispers_of_ether:multi_jump" -> 50.0;
            default -> 1.0;
        };
    }

    private static double getMaxAttributeValue(ResourceLocation attributeId) {
        return MAX_STAT_WEIGHT / getAttributeWeight(attributeId);
    }

    private record Probabilities(double criticalSuccess, double neutralSuccess, double criticalFailure) {}
}
