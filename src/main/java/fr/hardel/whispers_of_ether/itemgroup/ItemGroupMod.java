package fr.hardel.whispers_of_ether.itemgroup;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ItemGroupMod {
    public static final ResourceKey<CreativeModeTab> CUSTOM_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "item_group"));

    public static final CreativeModeTab CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.RUNE_ATTACK_SPEED))
            .title(Component.translatable("itemGroup.whispers_of_ether"))
            .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
    }
}
