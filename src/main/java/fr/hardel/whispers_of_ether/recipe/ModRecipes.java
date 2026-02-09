package fr.hardel.whispers_of_ether.recipe;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RecipeType<RunicForgeRecipe> RUNIC_FORGE_TYPE = Registry.register(
        BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_forge"),
        new RecipeType<RunicForgeRecipe>() {
            @Override
            public String toString() {
                return "runic_forge";
            }
        }
    );

    public static final RecipeSerializer<RunicForgeRecipe> RUNIC_FORGE_SERIALIZER = Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_forge"),
        new RunicForgeRecipe.Serializer()
    );

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering recipes for {}", WhispersOfEther.MOD_ID);
    }
}
