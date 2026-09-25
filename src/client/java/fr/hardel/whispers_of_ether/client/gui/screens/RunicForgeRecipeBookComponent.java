package fr.hardel.whispers_of_ether.client.gui.screens;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.gui.ThemedGuiGraphics;
import fr.hardel.whispers_of_ether.world.inventory.RunicForgeMenu;
import fr.hardel.whispers_of_ether.world.item.crafting.ModRecipes;
import fr.hardel.whispers_of_ether.world.level.block.ModBlocks;
import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.renderer.RenderPipelines;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunicForgeRecipeBookComponent extends RecipeBookComponent<RunicForgeMenu> {
    private static final Identifier PANEL = Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "textures/gui/container/runic_forge_recipe_book.png");
    private static final String SPRITE_FOLDER = "recipe_book/runic_forge/";
    private static final Map<Identifier, Identifier> SPRITES = Stream.of("slot_craftable", "slot_uncraftable", "slot_many_craftable", "slot_many_uncraftable", "tab", "tab_selected")
        .collect(Collectors.toMap(name -> Identifier.withDefaultNamespace("recipe_book/" + name), RunicForgeRecipeBookComponent::sprite));
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(sprite("filter_enabled"), sprite("filter_disabled"), sprite("filter_enabled_highlighted"),
        sprite("filter_disabled_highlighted"));
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final int PAGE_TEXT_COLOR = 0xFF404040;

    public RunicForgeRecipeBookComponent(RunicForgeMenu menu) {
        super(menu, List.of(new RecipeBookComponent.TabInfo(ModBlocks.RUNIC_FORGE.asItem(), ModRecipes.RUNIC_FORGE_CATEGORY)));
    }

    private static Identifier sprite(String name) {
        return Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, SPRITE_FOLDER + name);
    }

    // Same drawing as vanilla, with the forge panel and the tabs and recipe page drawn through the forge theme.
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        if (!this.isVisible()) {
            return;
        }

        if (!this.minecraft.hasControlDown()) {
            this.time += a;
        }

        int xo = this.getXOrigin();
        int yo = this.getYOrigin();
        graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL, xo, yo, 1.0F, 1.0F, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
        this.searchBox.extractRenderState(graphics, mouseX, mouseY, a);

        GuiGraphicsExtractor themed = new ThemedGuiGraphics(this.minecraft, graphics, mouseX, mouseY, SPRITES, PAGE_TEXT_COLOR);
        this.tabButtons.forEach(tab -> tab.extractRenderState(themed, mouseX, mouseY, a));
        this.filterButton.extractRenderState(graphics, mouseX, mouseY, a);
        this.recipeBookPage.extractRenderState(themed, xo, yo, mouseX, mouseY, a);
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
