package fr.hardel.whispers_of_ether.world.item.crafting;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RunicForgeInput(List<ItemStack> items, StackedItemContents contents) implements RecipeInput {

    public static RunicForgeInput of(List<ItemStack> items) {
        StackedItemContents contents = new StackedItemContents();
        items.forEach(contents::accountStack);
        return new RunicForgeInput(items, contents);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public int size() {
        return items.size();
    }
}
