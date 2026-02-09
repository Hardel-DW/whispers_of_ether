package fr.hardel.whispers_of_ether.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class RunicForgeRecipe implements Recipe<RecipeInput> {
    private final List<Ingredient> ingredients;
    private final Ingredient input;
    private final ItemStack result;

    public RunicForgeRecipe(List<Ingredient> ingredients, Ingredient input, ItemStack result) {
        this.ingredients = ingredients;
        this.input = input;
        this.result = result;
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        if (recipeInput.size() != 6) {
            return false;
        }

        if (!input.test(recipeInput.getItem(5))) {
            return false;
        }

        boolean[] ingredientMatched = new boolean[ingredients.size()];
        for (int i = 0; i < 5; i++) {
            ItemStack runeStack = recipeInput.getItem(i);
            if (runeStack.isEmpty()) {
                continue;
            }

            boolean matched = false;
            for (int j = 0; j < ingredients.size(); j++) {
                if (!ingredientMatched[j] && ingredients.get(j).test(runeStack)) {
                    ingredientMatched[j] = true;
                    matched = true;
                    break;
                }
            }

            if (!matched && !runeStack.isEmpty()) {
                return false;
            }
        }

        for (int i = 0; i < ingredients.size(); i++) {
            if (!ingredientMatched[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return ModRecipes.RUNIC_FORGE_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return ModRecipes.RUNIC_FORGE_TYPE;
    }

    public List<Ingredient> ingredients() {
        return ingredients;
    }

    public Ingredient input() {
        return input;
    }

    public ItemStack result() {
        return result;
    }

    public static class Serializer implements RecipeSerializer<RunicForgeRecipe> {
        private static final MapCodec<RunicForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(
            r -> r.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(RunicForgeRecipe::ingredients),
                Ingredient.CODEC.fieldOf("input").forGetter(RunicForgeRecipe::input),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RunicForgeRecipe::result)).apply(r, RunicForgeRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, RunicForgeRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
            RunicForgeRecipe::ingredients,
            Ingredient.CONTENTS_STREAM_CODEC,
            RunicForgeRecipe::input,
            ItemStack.STREAM_CODEC,
            RunicForgeRecipe::result,
            RunicForgeRecipe::new);

        @Override
        public MapCodec<RunicForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RunicForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
