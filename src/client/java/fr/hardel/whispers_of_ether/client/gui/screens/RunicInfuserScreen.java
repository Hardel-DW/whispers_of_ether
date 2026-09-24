package fr.hardel.whispers_of_ether.client.gui.screens;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.world.inventory.RunicInfuserMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class RunicInfuserScreen extends AbstractContainerScreen<RunicInfuserMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/container/runic_infusuer.png");
    private static final int IMAGE_WIDTH = 181;
    private static final int IMAGE_HEIGHT = 177;
    private static final int ASSET_SIZE_X = 512;
    private static final int ASSET_SIZE_Y = 256;

    public RunicInfuserScreen(RunicInfuserMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, ASSET_SIZE_X, ASSET_SIZE_Y);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
