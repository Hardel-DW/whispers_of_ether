package fr.hardel.whispers_of_ether.client.gui;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.cursor.CursorType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

// Draws into the same render state as its parent, with vanilla sprites and white text swapped for a theme.
public class ThemedGuiGraphics extends GuiGraphicsExtractor {
    private static final int WHITE = -1;
    private final GuiGraphicsExtractor parent;
    private final Map<Identifier, Identifier> sprites;
    private final int textColor;

    public ThemedGuiGraphics(Minecraft minecraft, GuiGraphicsExtractor parent, int mouseX, int mouseY, Map<Identifier, Identifier> sprites, int textColor) {
        super(minecraft, parent.guiRenderState, mouseX, mouseY);
        this.parent = parent;
        this.sprites = sprites;
        this.textColor = textColor;
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, int color) {
        super.blitSprite(renderPipeline, sprites.getOrDefault(location, location), x, y, width, height, color);
    }

    @Override
    public void blitSprite(RenderPipeline renderPipeline, Identifier location, int spriteWidth, int spriteHeight, int textureX, int textureY, int x, int y, int width,
        int height, int color) {
        super.blitSprite(renderPipeline, sprites.getOrDefault(location, location), spriteWidth, spriteHeight, textureX, textureY, x, y, width, height, color);
    }

    @Override
    public void text(Font font, @Nullable String str, int x, int y, int color) {
        if (color == WHITE) {
            super.text(font, str, x, y, textColor, false);
            return;
        }

        super.text(font, str, x, y, color);
    }

    @Override
    public void requestCursor(CursorType cursorType) {
        parent.requestCursor(cursorType);
    }
}
