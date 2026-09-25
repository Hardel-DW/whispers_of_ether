package fr.hardel.whispers_of_ether.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import fr.hardel.whispers_of_ether.world.level.block.ModBlocks;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RunicForgeRecipe implements Recipe<CraftingInput> {
    public static final MapCodec<RunicForgeRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
        r -> r.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(RunicForgeRecipe::ingredients),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(RunicForgeRecipe::result)).apply(r, RunicForgeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RunicForgeRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
        RunicForgeRecipe::ingredients,
        ItemStackTemplate.STREAM_CODEC,
        RunicForgeRecipe::result,
        RunicForgeRecipe::new);

    private final List<Ingredient> ingredients;
    private final ItemStackTemplate result;
    private @Nullable PlacementInfo placementInfo;

    public RunicForgeRecipe(List<Ingredient> ingredients, ItemStackTemplate result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return input.ingredientCount() == ingredients.size() && input.stackedContents().canCraft(this, null);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NotNull String group() {
        return "";
    }

    @Override
    public @NotNull RecipeSerializer<RunicForgeRecipe> getSerializer() {
        return ModRecipes.RUNIC_FORGE_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<RunicForgeRecipe> getType() {
        return ModRecipes.RUNIC_FORGE_TYPE;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        if (placementInfo == null) {
            placementInfo = PlacementInfo.create(ingredients);
        }
        return placementInfo;
    }

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(ingredients.stream().map(Ingredient::display).toList(), new SlotDisplay.ItemStackSlotDisplay(result),
            new SlotDisplay.ItemSlotDisplay(ModBlocks.RUNIC_FORGE.asItem())));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ModRecipes.RUNIC_FORGE_CATEGORY;
    }

    public List<Ingredient> ingredients() {
        return ingredients;
    }

    public ItemStackTemplate result() {
        return result;
    }
}
