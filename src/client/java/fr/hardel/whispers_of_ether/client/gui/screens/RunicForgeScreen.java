package fr.hardel.whispers_of_ether.client.gui.screens;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.world.inventory.RunicForgeMenu;
import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class RunicForgeScreen extends AbstractRecipeBookScreen<RunicForgeMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/container/runic_forge.png");
    private static final Identifier PROGRESS_BAR = Identifier.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/sprites/container/runic_forge/progress_bar.png");
    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 176;
    private static final int ASSET_SIZE_X = 512;
    private static final int ASSET_SIZE_Y = 256;
    private static final int PROGRESS_BAR_WIDTH = 5;
    private static final int PROGRESS_BAR_HEIGHT = 71;
    private static final int PROGRESS_BAR_X = 164;
    private static final int PROGRESS_BAR_Y = 10;
    private static final int RECIPE_BOOK_BUTTON_X = 20;
    private static final int RECIPE_BOOK_BUTTON_Y = 33;

    public RunicForgeScreen(RunicForgeMenu menu, Inventory inventory, Component title) {
        super(menu, new RunicForgeRecipeBookComponent(menu), inventory, title);
        this.imageWidth = IMAGE_WIDTH;
        this.imageHeight = IMAGE_HEIGHT;
        this.inventoryLabelY = IMAGE_HEIGHT - 94;
    }

    @Override
    protected @NotNull ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + RECIPE_BOOK_BUTTON_X, this.topPos + RECIPE_BOOK_BUTTON_Y);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, ASSET_SIZE_X, ASSET_SIZE_Y);

        int progress = (menu.getProcessProgress() * PROGRESS_BAR_HEIGHT) / RunicForgeBlockEntity.PROCESS_TIME;
        if (progress > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, PROGRESS_BAR,
                leftPos + PROGRESS_BAR_X,
                topPos + PROGRESS_BAR_Y + (PROGRESS_BAR_HEIGHT - progress),
                0,
                PROGRESS_BAR_HEIGHT - progress,
                PROGRESS_BAR_WIDTH,
                progress,
                PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
