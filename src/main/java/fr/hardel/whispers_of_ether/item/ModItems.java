package fr.hardel.whispers_of_ether.item;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import fr.hardel.whispers_of_ether.component.ModItemComponent;

public class ModItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhispersOfEther.MOD_ID);
    public static final Item RUNE_ATTACK_SPEED = register("rune_attack_speed", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_EXP = register("rune_exp", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_EXP_JOB = register("rune_exp_job", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_FALL = register("rune_fall", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_MONEY_JOB = register("rune_money_job", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_OTHER = register("rune_other", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_SATURATION = register("rune_saturation", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_SPEED = register("rune_speed", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_STRENGTH = register("rune_strength", RuneItem::new,
            new Item.Properties().component(ModItemComponent.RUNES, 1));

    public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
        Item item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {
        LOGGER.info("Registering Mod Items for {}", WhispersOfEther.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroupMod.CUSTOM_ITEM_GROUP_KEY).register(content -> {
            content.accept(RUNE_ATTACK_SPEED);
            content.accept(RUNE_EXP);
            content.accept(RUNE_EXP_JOB);
            content.accept(RUNE_FALL);
            content.accept(RUNE_MONEY_JOB);
            content.accept(RUNE_OTHER);
            content.accept(RUNE_SATURATION);
            content.accept(RUNE_SPEED);
            content.accept(RUNE_STRENGTH);
        });
    }
}
