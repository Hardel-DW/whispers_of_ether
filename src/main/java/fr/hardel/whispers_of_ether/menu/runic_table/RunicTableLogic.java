package fr.hardel.whispers_of_ether.menu.runic_table;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import fr.hardel.whispers_of_ether.component.item.WellComponent;
import fr.hardel.whispers_of_ether.runic_attribute.AttributeData;
import fr.hardel.whispers_of_ether.runic_attribute.AttributeDataLoader;
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

import java.util.*;
import java.util.stream.Collectors;

public class RunicTableLogic {
    private static final Random RANDOM = new Random();

    public enum Outcome {
        CRITICAL_SUCCESS,
        NEUTRAL_SUCCESS,
        CRITICAL_FAILURE,
        BLOCKED
    }

    public record RunicTableResult(Outcome outcome, ItemStack resultStack, String message, List<RunicTableHistoryEntry.StatChange> statChanges) {}

    public static RunicTableResult applyRune(ItemStack runeStack, ItemStack equipmentStack) {
        RuneComponent rune = runeStack.get(ModItemComponent.RUNES);
        if (rune == null) {
            return new RunicTableResult(Outcome.BLOCKED, equipmentStack, "runic_table.whispers_of_ether.no_rune", List.of());
        }

        Optional<AttributeData> runeDataOpt = AttributeDataLoader.getAttribute(rune.runeId());
        if (runeDataOpt.isEmpty()) {
            return new RunicTableResult(Outcome.BLOCKED, equipmentStack, "runic_table.whispers_of_ether.invalid_rune", List.of());
        }

        AttributeData runeData = runeDataOpt.get();
        WellComponent well = equipmentStack.getOrDefault(ModItemComponent.WELL, WellComponent.EMPTY);

        double currentValue = getAttributeValue(equipmentStack, runeData.attribute(), runeData.operation());
        double maxValue = runeData.maxValue();
        boolean inverted = maxValue < 0;

        if (inverted ? currentValue <= maxValue : currentValue >= maxValue) {
            return new RunicTableResult(Outcome.BLOCKED, equipmentStack, "runic_table.whispers_of_ether.max_stat", List.of());
        }

        int attributeCount = (int) getOrInitModifiers(equipmentStack).modifiers().stream()
            .map(entry -> BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value()))
            .distinct()
            .count();
        double wellConsumed = calculateWellConsumption(runeData.weight(), currentValue, maxValue, attributeCount);

        if (well.currentWell() < wellConsumed) {
            return new RunicTableResult(Outcome.BLOCKED, equipmentStack, "runic_table.whispers_of_ether.insufficient_well", List.of());
        }

        WellComponent newWell = well.consume(wellConsumed);
        double totalItemWeight = calculateTotalWeight(equipmentStack);

        Probabilities prob = calculateProbabilities(rune.tier(), currentValue, maxValue, newWell.getWellFactor());
        Outcome outcome = determineOutcome(prob);

        List<RunicTableHistoryEntry.StatChange> statChanges = new ArrayList<>();
        ItemStack result = applyOutcome(equipmentStack, runeData, outcome, totalItemWeight, statChanges);
        result.set(ModItemComponent.WELL, newWell);

        String message = switch (outcome) {
            case CRITICAL_SUCCESS -> "runic_table.whispers_of_ether.critical_success";
            case NEUTRAL_SUCCESS -> "runic_table.whispers_of_ether.neutral_success";
            case CRITICAL_FAILURE -> "runic_table.whispers_of_ether.critical_failure";
            case BLOCKED -> "runic_table.whispers_of_ether.blocked";
        };

        return new RunicTableResult(outcome, result, message, statChanges);
    }

    private static double calculateWellConsumption(double runeWeight, double currentValue, double maxValue, int attributeCount) {
        double currentStatLevel = maxValue != 0 ? Math.abs(currentValue / maxValue) : 0;
        double baseCost = (runeWeight * 0.15) + attributeCount;
        return baseCost + (runeWeight * currentStatLevel);
    }

    private static Probabilities calculateProbabilities(int tier, double currentValue, double maxValue,
        double wellFactor) {
        double baseProb = 0.15 + (tier * 0.15);
        double penaltyMax = maxValue != 0 ? Math.abs(currentValue / maxValue) * 0.5 : 0;
        double penaltyWell = wellFactor < 0.2 ? 0.3 : (wellFactor < 0.5 ? 0.1 : 0);
        double critSuccess = baseProb * (1 - penaltyMax) * 0.15;
        double neutralSuccess = baseProb * (1 - penaltyMax) * (1 - penaltyWell);
        double critFailure = 1 - critSuccess - neutralSuccess;

        return new Probabilities(
            Math.max(0, Math.min(1, critSuccess)),
            Math.max(0, Math.min(1, neutralSuccess)),
            Math.max(0, Math.min(1, critFailure)));
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

    private static ItemStack applyOutcome(ItemStack equipment, AttributeData runeData, Outcome outcome,
        double totalWeight, List<RunicTableHistoryEntry.StatChange> statChanges) {
        ItemStack result = equipment.copy();

        double statGain = switch (outcome) {
            case CRITICAL_SUCCESS, NEUTRAL_SUCCESS -> {
                double min = runeData.onPass().min();
                double max = runeData.onPass().max();
                if (max < min) {
                    double temp = min;
                    min = max;
                    max = temp;
                }
                double randomValue = min + RANDOM.nextDouble() * (max - min);
                yield Math.round(randomValue * 1000.0) / 1000.0;
            }
            case CRITICAL_FAILURE, BLOCKED -> 0;
        };

        if (statGain != 0) {
            addAttributeModifier(result, runeData.attribute(), runeData.operation(), statGain);
            boolean isPositive = isPositiveChange(runeData.attribute(), statGain);
            statChanges.add(new RunicTableHistoryEntry.StatChange(runeData.attribute(), statGain, runeData.operation(), isPositive));
        }

        if (outcome == Outcome.NEUTRAL_SUCCESS || outcome == Outcome.CRITICAL_FAILURE) {
            double lossMultiplier = outcome == Outcome.NEUTRAL_SUCCESS ? 0.1 : 0.2;
            applyStatLoss(result, runeData.attribute(), runeData.weight(), totalWeight, lossMultiplier, outcome, statChanges);
        }

        return result;
    }

    private static boolean isPositiveChange(ResourceLocation attributeId, double delta) {
        Optional<Holder.Reference<Attribute>> holder = BuiltInRegistries.ATTRIBUTE.get(
            ResourceKey.create(Registries.ATTRIBUTE, attributeId));
        if (holder.isEmpty())
            return delta > 0;

        Attribute attr = holder.get().value();
        return attr.getStyle(delta > 0) == net.minecraft.ChatFormatting.BLUE;
    }

    private static void addAttributeModifier(ItemStack stack, ResourceLocation attributeId,
        AttributeModifier.Operation operation, double valueToAdd) {
        ItemAttributeModifiers modifiers = getOrInitModifiers(stack);
        List<ItemAttributeModifiers.Entry> entries = new ArrayList<>(modifiers.modifiers());

        Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.get(
            ResourceKey.create(Registries.ATTRIBUTE, attributeId));

        if (attributeHolder.isEmpty())
            return;
        Holder<Attribute> attribute = attributeHolder.get();
        ItemAttributeModifiers.Entry existingEntry = null;
        double currentAmount = 0.0;

        for (ItemAttributeModifiers.Entry entry : entries) {
            if (entry.attribute().value().equals(attribute.value()) && entry.modifier().operation() == operation) {
                existingEntry = entry;
                currentAmount = entry.modifier().amount();
                break;
            }
        }

        if (existingEntry != null) {
            entries.remove(existingEntry);
            entries.add(new ItemAttributeModifiers.Entry(
                existingEntry.attribute(),
                new AttributeModifier(existingEntry.modifier().id(), currentAmount + valueToAdd, operation),
                existingEntry.slot()));
        } else {
            EquipmentSlotGroup slot = entries.isEmpty()
                ? EquipmentSlotGroup.ANY
                : entries.get(0).slot();

            ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                attributeId.getNamespace(),
                attributeId.getPath() + "_" + slot.getSerializedName());

            entries.add(new ItemAttributeModifiers.Entry(
                attribute,
                new AttributeModifier(modifierId, valueToAdd, operation),
                slot));
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(entries));
    }

    private static void applyStatLoss(ItemStack stack, ResourceLocation excludeAttribute, double runeWeight,
        double totalWeight, double multiplier, Outcome outcome, List<RunicTableHistoryEntry.StatChange> statChanges) {
        ItemAttributeModifiers modifiers = getOrInitModifiers(stack);
        if (totalWeight <= 0) {
            return;
        }

        List<ItemAttributeModifiers.Entry> eligibleForLoss = modifiers.modifiers().stream()
            .filter(entry -> !Objects.equals(BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value()), excludeAttribute))
            .toList();

        if (eligibleForLoss.isEmpty()) {
            return;
        }

        int maxAffected = outcome == Outcome.CRITICAL_FAILURE
            ? 1 + RANDOM.nextInt(3)
            : 1 + RANDOM.nextInt(2);
        int affected = Math.min(maxAffected, eligibleForLoss.size());

        List<ItemAttributeModifiers.Entry> toAffect = new ArrayList<>(eligibleForLoss);
        Collections.shuffle(toAffect, RANDOM);
        Set<ResourceLocation> affectedAttributes = toAffect.stream()
            .limit(affected)
            .map(e -> BuiltInRegistries.ATTRIBUTE.getKey(e.attribute().value()))
            .collect(Collectors.toSet());

        double lossFactor = (runeWeight / totalWeight) * multiplier;
        List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> holder = entry.attribute();
            ResourceLocation attrId = BuiltInRegistries.ATTRIBUTE.getKey(holder.value());

            if (affectedAttributes.contains(attrId)) {
                double currentValue = entry.modifier().amount();
                double newValue = currentValue > 0
                    ? currentValue * (1 - lossFactor)
                    : currentValue * (1 + lossFactor);
                double delta = newValue - currentValue;
                if (Math.abs(newValue) > 0.001) {
                    newEntries.add(new ItemAttributeModifiers.Entry(holder, new AttributeModifier(entry.modifier().id(), newValue, entry.modifier().operation()), entry.slot()));
                    boolean isPositive = isPositiveChange(attrId, delta);
                    statChanges.add(new RunicTableHistoryEntry.StatChange(attrId, delta, entry.modifier().operation(), isPositive));
                }
            } else {
                newEntries.add(entry);
            }
        }

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(newEntries));
    }

    private static double getAttributeValue(ItemStack stack, ResourceLocation attributeId, AttributeModifier.Operation operation) {
        ItemAttributeModifiers modifiers = getOrInitModifiers(stack);

        return modifiers.modifiers().stream()
            .filter(entry -> Objects.equals(BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value()), attributeId))
            .filter(entry -> entry.modifier().operation() == operation)
            .mapToDouble(entry -> entry.modifier().amount())
            .sum();
    }

    private static double calculateTotalWeight(ItemStack stack) {
        ItemAttributeModifiers modifiers = getOrInitModifiers(stack);

        return modifiers.modifiers().stream()
            .mapToDouble(entry -> {
                double value = entry.modifier().amount();
                ResourceLocation attrId = BuiltInRegistries.ATTRIBUTE.getKey(entry.attribute().value());
                assert attrId != null;
                double weight = getAttributeWeightFromData(attrId);
                return Math.abs(value) * weight;
            })
            .sum();
    }

    private static ItemAttributeModifiers getOrInitModifiers(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, stack.getItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY));
    }

    private static double getAttributeWeightFromData(ResourceLocation attributeId) {
        return AttributeDataLoader.getAttributes().values().stream()
            .filter(data -> data.attribute().equals(attributeId))
            .findFirst()
            .map(AttributeData::weight)
            .orElse(1.0);
    }

    private record Probabilities(double criticalSuccess, double neutralSuccess, double criticalFailure) {}
}
