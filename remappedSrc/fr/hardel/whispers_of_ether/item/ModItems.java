package fr.hardel.whispers_of_ether.item;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import fr.hardel.whispers_of_ether.component.ModItemComponent;

public class ModItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhispersOfEther.MOD_ID);
    public static final Item RUNE_ATTACK_SPEED = register("rune_attack_speed", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_EXP = register("rune_exp", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_EXP_JOB = register("rune_exp_job", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_FALL = register("rune_fall", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_MONEY_JOB = register("rune_money_job", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_OTHER = register("rune_other", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_SATURATION = register("rune_saturation", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_SPEED = register("rune_speed", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));
    public static final Item RUNE_STRENGTH = register("rune_strength", RuneItem::new,
            new Item.Settings().component(ModItemComponent.RUNES, 1));

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(WhispersOfEther.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {
        LOGGER.info("Registering Mod Items for {}", WhispersOfEther.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroupMod.CUSTOM_ITEM_GROUP_KEY).register(content -> {
            content.add(RUNE_ATTACK_SPEED);
            content.add(RUNE_EXP);
            content.add(RUNE_EXP_JOB);
            content.add(RUNE_FALL);
            content.add(RUNE_MONEY_JOB);
            content.add(RUNE_OTHER);
            content.add(RUNE_SATURATION);
            content.add(RUNE_SPEED);
            content.add(RUNE_STRENGTH);
        });
    }
}
