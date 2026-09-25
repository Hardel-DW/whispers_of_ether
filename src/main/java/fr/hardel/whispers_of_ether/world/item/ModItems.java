package fr.hardel.whispers_of_ether.world.item;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.Map;
import fr.hardel.whispers_of_ether.world.item.component.DataComponent;
import fr.hardel.whispers_of_ether.world.item.component.RuneComponent;

public class ModItems {
    public static final Item RUNE = register("rune", Item::new, new Item.Properties());
    public static final Item REINFORCED_RUNE = register("reinforced_rune", Item::new, new Item.Properties());
    public static final Item ECHO_RUNE = register("echo_rune", Item::new, new Item.Properties());
    public static final Item NETHER_RUNE = register("nether_rune", Item::new, new Item.Properties());
    public static final Item DRAGON_RUNE = register("dragon_rune", Item::new, new Item.Properties());
    public static final Item RUNE_OF_ARMOR = register("rune_of_armor", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_ARMOR_TOUGHNESS = register("rune_of_armor_toughness", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_ATTACK_DAMAGE = register("rune_of_attack_damage", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_ATTACK_KNOCKBACK = register("rune_of_attack_knockback", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_ATTACK_SPEED = register("rune_of_attack_speed", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_BLOCK_BREAK_SPEED = register("rune_of_block_break_speed", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_BLOCK_INTERACTION_RANGE = register("rune_of_block_interaction_range", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_BURNING_TIME = register("rune_of_burning_time", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_CRIT_DAMAGE = register("rune_of_crit_damage", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_CRIT_RATE = register("rune_of_crit_rate", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_ENTITY_INTERACTION_RANGE = register("rune_of_entity_interaction_range", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_FALL_DAMAGE_MULTIPLIER = register("rune_of_fall_damage_multiplier", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_JUMP_STRENGTH = register("rune_of_jump_strength", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_KNOCKBACK_RESISTANCE = register("rune_of_knockback_resistance", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_MAX_HEALTH = register("rune_of_max_health", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_MINING_EFFICIENCY = register("rune_of_mining_efficiency", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_MOVEMENT_SPEED = register("rune_of_movement_speed", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_MULTI_JUMP = register("rune_of_multi_jump", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_OMNIVAMPIRISM = register("rune_of_omnivampirism", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_OMNIVAMPIRISM_RATE = register("rune_of_omnivampirism_rate", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_PICKUP_AREA_SIZE = register("rune_of_pickup_area_size", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_SAFE_FALL_DISTANCE = register("rune_of_safe_fall_distance", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_SNEAKING_SPEED = register("rune_of_sneaking_speed", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_STEP_HEIGHT = register("rune_of_step_height", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_SWEEPING_DAMAGE_RATIO = register("rune_of_sweeping_damage_ratio", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OF_WATER_MOVEMENT_EFFICIENCY = register("rune_of_water_movement_efficiency", Item::new,
        new Item.Properties().component(DataComponent.RUNES, RuneComponent.EMPTY));

    public static final Item TARGET_DUMMY = register("target_dummy", TargetDummyItem::new, new Item.Properties());

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static final Map<Item, Identifier> RUNE_TO_DATA = Map.ofEntries(
        Map.entry(RUNE_OF_ARMOR, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "armor_flat")),
        Map.entry(RUNE_OF_ARMOR_TOUGHNESS, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "armor_toughness_percent")),
        Map.entry(RUNE_OF_ATTACK_DAMAGE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "attack_damage_flat")),
        Map.entry(RUNE_OF_ATTACK_KNOCKBACK, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "attack_knockback_percent")),
        Map.entry(RUNE_OF_ATTACK_SPEED, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "attack_speed_percent")),
        Map.entry(RUNE_OF_BLOCK_BREAK_SPEED, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "block_break_speed_percent")),
        Map.entry(RUNE_OF_BLOCK_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "block_interaction_range_flat")),
        Map.entry(RUNE_OF_BURNING_TIME, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "burning_time_percent")),
        Map.entry(RUNE_OF_CRIT_DAMAGE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "crit_damage_percent")),
        Map.entry(RUNE_OF_CRIT_RATE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "crit_rate_percent")),
        Map.entry(RUNE_OF_ENTITY_INTERACTION_RANGE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "entity_interaction_range_flat")),
        Map.entry(RUNE_OF_FALL_DAMAGE_MULTIPLIER, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "fall_damage_multiplier_percent")),
        Map.entry(RUNE_OF_JUMP_STRENGTH, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "jump_strength_percent")),
        Map.entry(RUNE_OF_KNOCKBACK_RESISTANCE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "knockback_resistance_percent")),
        Map.entry(RUNE_OF_MAX_HEALTH, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "max_health_flat")),
        Map.entry(RUNE_OF_MINING_EFFICIENCY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "mining_efficiency_percent")),
        Map.entry(RUNE_OF_MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "movement_speed_percent")),
        Map.entry(RUNE_OF_MULTI_JUMP, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "multi_jump_flat")),
        Map.entry(RUNE_OF_OMNIVAMPIRISM, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "omnivampirism_percent")),
        Map.entry(RUNE_OF_OMNIVAMPIRISM_RATE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "omnivampirism_rate_percent")),
        Map.entry(RUNE_OF_PICKUP_AREA_SIZE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "pickup_area_size_flat")),
        Map.entry(RUNE_OF_SAFE_FALL_DISTANCE, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "safe_fall_distance_flat")),
        Map.entry(RUNE_OF_SNEAKING_SPEED, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "sneaking_speed_percent")),
        Map.entry(RUNE_OF_STEP_HEIGHT, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "step_height_flat")),
        Map.entry(RUNE_OF_SWEEPING_DAMAGE_RATIO, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "sweeping_damage_ratio_percent")),
        Map.entry(RUNE_OF_WATER_MOVEMENT_EFFICIENCY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "water_movement_efficiency_percent")));

    public static void register() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.WHISPERSOFETHER_RUNES_GROUP_KEY).register(content -> {
            content.accept(RUNE);
            content.accept(REINFORCED_RUNE);
            content.accept(ECHO_RUNE);
            content.accept(NETHER_RUNE);
            content.accept(DRAGON_RUNE);
            registerRuneWithTiers(content, RUNE_OF_ARMOR);
            registerRuneWithTiers(content, RUNE_OF_ARMOR_TOUGHNESS);
            registerRuneWithTiers(content, RUNE_OF_ATTACK_DAMAGE);
            registerRuneWithTiers(content, RUNE_OF_ATTACK_KNOCKBACK);
            registerRuneWithTiers(content, RUNE_OF_ATTACK_SPEED);
            registerRuneWithTiers(content, RUNE_OF_BLOCK_BREAK_SPEED);
            registerRuneWithTiers(content, RUNE_OF_BLOCK_INTERACTION_RANGE);
            registerRuneWithTiers(content, RUNE_OF_BURNING_TIME);
            registerRuneWithTiers(content, RUNE_OF_CRIT_DAMAGE);
            registerRuneWithTiers(content, RUNE_OF_CRIT_RATE);
            registerRuneWithTiers(content, RUNE_OF_ENTITY_INTERACTION_RANGE);
            registerRuneWithTiers(content, RUNE_OF_FALL_DAMAGE_MULTIPLIER);
            registerRuneWithTiers(content, RUNE_OF_JUMP_STRENGTH);
            registerRuneWithTiers(content, RUNE_OF_KNOCKBACK_RESISTANCE);
            registerRuneWithTiers(content, RUNE_OF_MAX_HEALTH);
            registerRuneWithTiers(content, RUNE_OF_MINING_EFFICIENCY);
            registerRuneWithTiers(content, RUNE_OF_MOVEMENT_SPEED);
            registerRuneWithTiers(content, RUNE_OF_MULTI_JUMP);
            registerRuneWithTiers(content, RUNE_OF_OMNIVAMPIRISM);
            registerRuneWithTiers(content, RUNE_OF_OMNIVAMPIRISM_RATE);
            registerRuneWithTiers(content, RUNE_OF_PICKUP_AREA_SIZE);
            registerRuneWithTiers(content, RUNE_OF_SAFE_FALL_DISTANCE);
            registerRuneWithTiers(content, RUNE_OF_SNEAKING_SPEED);
            registerRuneWithTiers(content, RUNE_OF_STEP_HEIGHT);
            registerRuneWithTiers(content, RUNE_OF_SWEEPING_DAMAGE_RATIO);
            registerRuneWithTiers(content, RUNE_OF_WATER_MOVEMENT_EFFICIENCY);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.WHISPERSOFETHER_GENERAL_GROUP_KEY).register(content -> content.accept(TARGET_DUMMY));
    }

    private static void registerRuneWithTiers(FabricCreativeModeTabOutput content, Item item) {
        Identifier runeId = RUNE_TO_DATA.get(item);
        if (runeId == null)
            return;

        for (int tier = 1; tier <= 5; tier++) {
            ItemStack stack = new ItemStack(item);
            stack.set(DataComponent.RUNES, new RuneComponent(runeId, tier));
            content.accept(stack, tier == 5 ? CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS : CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        }
    }
}
