package fr.hardel.whispers_of_ether.client.gui.screen;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.menu.RunicForgeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RunicForgeScreen extends AbstractContainerScreen<RunicForgeMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/container/runic_forge.png");
    private static final ResourceLocation PROGRESS_BAR = ResourceLocation.fromNamespaceAndPath(
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

    public RunicForgeScreen(RunicForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = IMAGE_WIDTH;
        this.imageHeight = IMAGE_HEIGHT;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderType::guiTextured, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, ASSET_SIZE_X, ASSET_SIZE_Y);

        int progress = (menu.getProcessProgress() * PROGRESS_BAR_HEIGHT) / menu.getMaxProcessTime();
        if (progress > 0) {
            graphics.blit(RenderType::guiTextured, PROGRESS_BAR,
                x + PROGRESS_BAR_X,
                y + PROGRESS_BAR_Y + (PROGRESS_BAR_HEIGHT - progress),
                0,
                PROGRESS_BAR_HEIGHT - progress,
                PROGRESS_BAR_WIDTH,
                progress,
                PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }
}
