package fr.hardel.whispers_of_ether.item;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;

public class ModItems {
    // Runes
    public static final Item RUNE_ATTACK_SPEED = register("rune_attack_speed", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_EXP = register("rune_exp", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_EXP_JOB = register("rune_exp_job", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_FALL = register("rune_fall", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_MONEY_JOB = register("rune_money_job", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_OTHER = register("rune_other", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_SATURATION = register("rune_saturation", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_SPEED = register("rune_speed", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));
    public static final Item RUNE_STRENGTH = register("rune_strength", Item::new,
            new Item.Properties().component(ModItemComponent.RUNES, RuneComponent.EMPTY));

    // Flame items
    public static final Item FLAME_AXE = register("flame_axe", Item::new, new Item.Properties());
    public static final Item FLAME_BOW = register("flame_bow", BowItem::new, new Item.Properties());
    public static final Item FLAME_CRYSTAL = register("flame_crystal", Item::new, new Item.Properties());
    public static final Item FLAME_ESSENCE = register("flame_essence", Item::new, new Item.Properties());
    public static final Item FLAME_FAST_SWORD = register("flame_fast_sword", Item::new, new Item.Properties());
    public static final Item FLAME_SWORD = register("flame_sword", Item::new, new Item.Properties());

    // Ice items
    public static final Item FROSTED_DIAMOND = register("frosted_diamond", Item::new, new Item.Properties());
    public static final Item FROST_FRAGMENT = register("frost_fragment", Item::new, new Item.Properties());
    public static final Item FROST_PICKAXE = register("frost_pickaxe", Item::new, new Item.Properties());
    public static final Item FROST_SWORD = register("frost_sword", Item::new, new Item.Properties());
    public static final Item FROZEN_BOW = register("frozen_bow", BowItem::new, new Item.Properties());
    public static final Item ICE_AXE = register("ice_axe", Item::new, new Item.Properties());
    public static final Item ICE_CUBE = register("ice_cube", Item::new, new Item.Properties());
    public static final Item ICE_HOE = register("ice_hoe", Item::new, new Item.Properties());
    public static final Item ICE_LEAF = register("ice_leaf", Item::new, new Item.Properties());
    public static final Item ICE_PICKAXE = register("ice_pickaxe", Item::new, new Item.Properties());
    public static final Item ICE_SHOVEL = register("ice_shovel", Item::new, new Item.Properties());
    public static final Item ICE_STAR = register("ice_star", Item::new, new Item.Properties());
    public static final Item ICE_TEARS = register("ice_tears", Item::new, new Item.Properties());
    public static final Item LONG_FROST_SWORD = register("long_frost_sword", Item::new, new Item.Properties());
    public static final Item SNOWFLAKE = register("snowflake", Item::new, new Item.Properties());
    public static final Item THREE_ICE_LEAF = register("three_ice_leaf", Item::new, new Item.Properties());

    // Ice and Fire items
    public static final Item FLAME_FROST_AXE = register("flame_frost_axe", Item::new, new Item.Properties());
    public static final Item FLAME_FROST_HOE = register("flame_frost_hoe", Item::new, new Item.Properties());
    public static final Item FLAME_FROST_PICKAXE = register("flame_frost_pickaxe", Item::new, new Item.Properties());
    public static final Item FLAME_FROST_SHOVEL = register("flame_frost_shovel", Item::new, new Item.Properties());
    public static final Item FLAME_FROST_SWORD = register("flame_frost_sword", Item::new, new Item.Properties());
    public static final Item FROSTED_FIRE_AXE = register("frosted_fire_axe", Item::new, new Item.Properties());
    public static final Item FROSTED_FIRE_HOE = register("frosted_fire_hoe", Item::new, new Item.Properties());
    public static final Item FROSTED_FIRE_PICKAXE = register("frosted_fire_pickaxe", Item::new, new Item.Properties());
    public static final Item FROSTED_FIRE_SHOVEL = register("frosted_fire_shovel", Item::new, new Item.Properties());
    public static final Item FROSTED_FIRE_SWORD = register("frosted_fire_sword", Item::new, new Item.Properties());
    public static final Item ICE_AND_FIRE_BOW = register("ice_and_fire_bow", BowItem::new, new Item.Properties());

    // Root items
    public static final Item AMETHYSM_CORAL_CRYSTAL = register("amethysm_coral_crystal", Item::new,
            new Item.Properties());
    public static final Item AUTNOM_LEAF = register("autnom_leaf", Item::new, new Item.Properties());
    public static final Item BIG_SCROLL = register("big_scroll", Item::new, new Item.Properties());
    public static final Item CARD = register("card", Item::new, new Item.Properties());
    public static final Item ENDERCUBE = register("endercube", Item::new, new Item.Properties());
    public static final Item SPRING_LEAF = register("spring_leaf", Item::new, new Item.Properties());
    public static final Item THREE_SPRING_LEAF = register("three_spring_leaf", Item::new, new Item.Properties());

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroupMod.CUSTOM_ITEM_GROUP_KEY).register(content -> {
            // Runes
            registerRuneWithTiers(content, RUNE_ATTACK_SPEED);
            registerRuneWithTiers(content, RUNE_EXP);
            registerRuneWithTiers(content, RUNE_EXP_JOB);
            registerRuneWithTiers(content, RUNE_FALL);
            registerRuneWithTiers(content, RUNE_MONEY_JOB);
            registerRuneWithTiers(content, RUNE_OTHER);
            registerRuneWithTiers(content, RUNE_SATURATION);
            registerRuneWithTiers(content, RUNE_SPEED);
            registerRuneWithTiers(content, RUNE_STRENGTH);

            // Flame items
            content.accept(FLAME_SWORD);
            content.accept(FLAME_FAST_SWORD);
            content.accept(FLAME_AXE);
            content.accept(FLAME_BOW);
            content.accept(FLAME_CRYSTAL);
            content.accept(FLAME_ESSENCE);

            // Ice items
            content.accept(FROST_SWORD);
            content.accept(LONG_FROST_SWORD);
            content.accept(FROZEN_BOW);
            content.accept(ICE_SHOVEL);
            content.accept(ICE_PICKAXE);
            content.accept(ICE_AXE);
            content.accept(ICE_HOE);
            content.accept(FROSTED_DIAMOND);
            content.accept(FROST_FRAGMENT);
            content.accept(FROST_PICKAXE);
            content.accept(ICE_CUBE);
            content.accept(ICE_LEAF);
            content.accept(ICE_STAR);
            content.accept(ICE_TEARS);
            content.accept(SNOWFLAKE);
            content.accept(THREE_ICE_LEAF);

            // Ice and Fire items
            content.accept(ICE_AND_FIRE_BOW);
            content.accept(FLAME_FROST_SWORD);
            content.accept(FLAME_FROST_SHOVEL);
            content.accept(FLAME_FROST_PICKAXE);
            content.accept(FLAME_FROST_AXE);
            content.accept(FLAME_FROST_HOE);
            content.accept(FROSTED_FIRE_SWORD);
            content.accept(FROSTED_FIRE_SHOVEL);
            content.accept(FROSTED_FIRE_PICKAXE);
            content.accept(FROSTED_FIRE_AXE);
            content.accept(FROSTED_FIRE_HOE);

            // Root items
            content.accept(AMETHYSM_CORAL_CRYSTAL);
            content.accept(AUTNOM_LEAF);
            content.accept(BIG_SCROLL);
            content.accept(CARD);
            content.accept(ENDERCUBE);
            content.accept(SPRING_LEAF);
            content.accept(THREE_SPRING_LEAF);
        });
    }

    private static void registerRuneWithTiers(FabricItemGroupEntries content, Item item) {
        for (int i = 1; i <= 5; i++) {
            ItemStack stack = new ItemStack(item);
            stack.set(ModItemComponent.RUNES, new RuneComponent(i));
            content.accept(stack);
        }
    }
}
