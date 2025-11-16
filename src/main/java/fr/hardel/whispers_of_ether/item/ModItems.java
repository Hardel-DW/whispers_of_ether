package fr.hardel.whispers_of_ether.item;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroupMod.CUSTOM_ITEM_GROUP_KEY).register(content -> {
            registerRuneWithTiers(content, RUNE_ATTACK_SPEED);
            registerRuneWithTiers(content, RUNE_EXP);
            registerRuneWithTiers(content, RUNE_EXP_JOB);
            registerRuneWithTiers(content, RUNE_FALL);
            registerRuneWithTiers(content, RUNE_MONEY_JOB);
            registerRuneWithTiers(content, RUNE_OTHER);
            registerRuneWithTiers(content, RUNE_SATURATION);
            registerRuneWithTiers(content, RUNE_SPEED);
            registerRuneWithTiers(content, RUNE_STRENGTH);
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
