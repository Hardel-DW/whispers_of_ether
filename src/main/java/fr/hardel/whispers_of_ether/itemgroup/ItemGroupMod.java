package fr.hardel.whispers_of_ether.itemgroup;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.block.ModBlocks;
import fr.hardel.whispers_of_ether.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class ItemGroupMod {
    public static final ResourceKey<CreativeModeTab> WHISPERSOFETHER_GENERAL_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "whispers_of_ether_general"));
    public static final ResourceKey<CreativeModeTab> WHISPERSOFETHER_RUNES_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "whispers_of_ether_runes"));

    public static final CreativeModeTab WHISPERSOFETHER_GENERAL_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModBlocks.RUNIC_FORGE))
        .title(Component.translatable("itemGroup.whispers_of_ether_general"))
        .build();

    public static final CreativeModeTab WHISPERSOFETHER_RUNES_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.RUNE_OF_ARMOR))
        .title(Component.translatable("itemGroup.whispers_of_ether_runes"))
        .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, WHISPERSOFETHER_GENERAL_GROUP_KEY, WHISPERSOFETHER_GENERAL_GROUP);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, WHISPERSOFETHER_RUNES_GROUP_KEY, WHISPERSOFETHER_RUNES_GROUP);
    }
}
