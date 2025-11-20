package fr.hardel.whispers_of_ether.block;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block FORGEMAGIC_BLOCK = register("forgemagic_block",
            ForgeMagicBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(3.0f)
                    .sound(SoundType.WOOD).noOcclusion()
                    .requiresCorrectToolForDrops(),
            true);

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
		ResourceKey<Block> blockKey = keyOfBlock(name);
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);
			BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
			Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

		return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroupMod.CUSTOM_ITEM_GROUP_KEY).register(content -> content.accept(FORGEMAGIC_BLOCK));
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name));
    }
}
