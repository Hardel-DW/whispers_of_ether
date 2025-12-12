package fr.hardel.whispers_of_ether.menu;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.menu.runic_table.RunicTableMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final MenuType<RunicTableMenu> RUNIC_TABLE = Registry.register(
        BuiltInRegistries.MENU,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "forge_magic"),
        new MenuType<>(RunicTableMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final MenuType<RunicForgeMenu> RUNIC_FORGE = Registry.register(
        BuiltInRegistries.MENU,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_forge"),
        new MenuType<>(RunicForgeMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final MenuType<RunicInfuserMenu> RUNIC_INFUSER = Registry.register(
        BuiltInRegistries.MENU,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_infuser"),
        new MenuType<>(RunicInfuserMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering menu types for {}", WhispersOfEther.MOD_ID);
    }
}
