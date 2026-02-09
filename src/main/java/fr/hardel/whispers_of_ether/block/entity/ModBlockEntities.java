package fr.hardel.whispers_of_ether.block.entity;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<RunicForgeBlockEntity> RUNIC_FORGE = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_forge"),
        BlockEntityType.Builder.of(RunicForgeBlockEntity::new, ModBlocks.RUNIC_FORGE)
            .build(null));

    public static final BlockEntityType<RunicInfuserBlockEntity> RUNIC_INFUSER = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_infuser"),
        BlockEntityType.Builder.of(RunicInfuserBlockEntity::new, ModBlocks.RUNIC_INFUSER)
            .build(null));

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering block entities for {}", WhispersOfEther.MOD_ID);
    }
}
