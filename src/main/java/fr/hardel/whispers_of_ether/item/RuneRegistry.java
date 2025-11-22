package fr.hardel.whispers_of_ether.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import java.util.Map;
import net.minecraft.world.item.Item;

public class RuneRegistry {
    private static final double[] TIER_VALUES = {1.0, 2.0, 4.0, 8.0, 16.0};

    public record RuneData(ResourceLocation attributeId, AttributeModifier.Operation operation, double weight) {}

    private static final Map<Item, RuneData> RUNE_DATA = Map.ofEntries(
        Map.entry(ModItems.RUNE_ATTACK_SPEED, new RuneData(
            Attributes.ATTACK_SPEED.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            10.0
        )),
        Map.entry(ModItems.RUNE_STRENGTH, new RuneData(
            Attributes.ATTACK_DAMAGE.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            20.0
        )),
        Map.entry(ModItems.RUNE_EXP, new RuneData(
            Attributes.ARMOR.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            5.0
        )),
        Map.entry(ModItems.RUNE_EXP_JOB, new RuneData(
            Attributes.ATTACK_SPEED.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            10.0
        )),
        Map.entry(ModItems.RUNE_FALL, new RuneData(
            Attributes.ATTACK_KNOCKBACK.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            10.0
        )),
        Map.entry(ModItems.RUNE_MONEY_JOB, new RuneData(
            Attributes.MAX_HEALTH.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            1.0
        )),
        Map.entry(ModItems.RUNE_OTHER, new RuneData(
            Attributes.MOVEMENT_SPEED.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
            200.0
        )),
        Map.entry(ModItems.RUNE_SATURATION, new RuneData(
            Attributes.KNOCKBACK_RESISTANCE.unwrapKey().get().location(),
            AttributeModifier.Operation.ADD_VALUE,
            20.0
        ))
    );

    public static RuneComponent createRuneComponent(Item runeItem, int tier) {
        RuneData data = RUNE_DATA.get(runeItem);
        if (data == null) {
            return RuneComponent.EMPTY;
        }

        int tierIndex = tier - 1;
        if (tierIndex < 0 || tierIndex >= TIER_VALUES.length) {
            return RuneComponent.EMPTY;
        }

        return new RuneComponent(
            data.attributeId,
            data.operation,
            data.weight,
            tier,
            TIER_VALUES[tierIndex]
        );
    }

    public static boolean isRune(Item item) {
        return RUNE_DATA.containsKey(item);
    }

    public static RuneData getRuneData(Item item) {
        return RUNE_DATA.get(item);
    }
}
