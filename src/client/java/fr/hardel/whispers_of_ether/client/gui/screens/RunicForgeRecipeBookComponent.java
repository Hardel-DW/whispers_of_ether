package fr.hardel.whispers_of_ether.client.gui.screens;

import fr.hardel.whispers_of_ether.world.inventory.RunicForgeMenu;
import fr.hardel.whispers_of_ether.world.item.crafting.ModRecipes;
import fr.hardel.whispers_of_ether.world.level.block.ModBlocks;
import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RunicForgeRecipeBookComponent extends RecipeBookComponent<RunicForgeMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
        Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
        Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
        Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");

    public RunicForgeRecipeBookComponent(RunicForgeMenu menu) {
        super(menu, List.of(new RecipeBookComponent.TabInfo(ModBlocks.RUNIC_FORGE.asItem(), ModRecipes.RUNIC_FORGE_CATEGORY)));
    }

    @Override
    protected @NotNull WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return slot.index <= RunicForgeBlockEntity.RESULT_SLOT;
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
        ghostSlots.setResult(this.menu.getSlot(RunicForgeBlockEntity.RESULT_SLOT), context, recipe.result());
        if (recipe instanceof ShapelessCraftingRecipeDisplay shapeless) {
            List<SlotDisplay> ingredients = shapeless.ingredients();
            for (int slot = 0; slot < ingredients.size(); slot++) {
                ghostSlots.setInput(this.menu.getSlot(slot), context, ingredients.get(slot));
            }
        }
    }

    @Override
    protected @NotNull Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
        collection.selectRecipes(stackedContents, display -> display instanceof ShapelessCraftingRecipeDisplay);
    }
}
