package fr.hardel.whispers_of_ether.menu;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final MenuType<ForgeMagicMenu> FORGE_MAGIC = Registry.register(
        BuiltInRegistries.MENU,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "forge_magic"),
        new MenuType<>(ForgeMagicMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering menu types for {}", WhispersOfEther.MOD_ID);
    }
}
