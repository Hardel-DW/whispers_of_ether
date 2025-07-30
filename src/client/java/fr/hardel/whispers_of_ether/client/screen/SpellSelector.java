package fr.hardel.whispers_of_ether.client.screen;

import fr.hardel.whispers_of_ether.client.WhispersOfEtherClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

public class SpellSelector {
    private static final Identifier SLOT_BACKGROUND = Identifier.of(WhispersOfEtherClient.MOD_ID,
            "textures/gui/sprites/hud/spell/slot_background.png");
    private static final Identifier SELECTED_TEXTURE = Identifier.of(WhispersOfEtherClient.MOD_ID,
            "textures/gui/sprites/hud/spell/selected.png");

    private static final int SLOT_SIZE = 20;
    private static final int GAP = 4;
    private static final int PADDING_LEFT = 4;
    private static final int SLOT_COUNT = 7;

    private static int selectedSlot = 0;
    private static float currentAnimationY = 0.0f;
    private static final float ANIMATION_SPEED = 0.2f;

    private static float currentHudX = -SLOT_SIZE - PADDING_LEFT;
    private static long lastActivityTime = 0;
    private static final long VISIBILITY_DURATION = 5000;
    private static final float SLIDE_SPEED = 0.15f;

    public static void render(DrawContext drawContext, RenderTickCounter ignoredTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null)
            return;

        int screenHeight = client.getWindow().getScaledHeight();
        int totalHeight = (SLOT_SIZE * SLOT_COUNT) + (GAP * (SLOT_COUNT - 1));
        int startY = (screenHeight - totalHeight) / 2;
        float targetY = startY + (selectedSlot * (SLOT_SIZE + GAP));
        currentAnimationY += (targetY - currentAnimationY) * ANIMATION_SPEED;

        long currentTime = System.currentTimeMillis();
        boolean shouldBeVisible = currentTime - lastActivityTime < VISIBILITY_DURATION;
        float targetHudX = shouldBeVisible ? PADDING_LEFT : -SLOT_SIZE - PADDING_LEFT;
        currentHudX += (targetHudX - currentHudX) * SLIDE_SPEED;

        for (int i = 0; i < SLOT_COUNT; i++) {
            int x = Math.round(currentHudX);
            int y = startY + (i * (SLOT_SIZE + GAP));

            drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE,
                    SLOT_SIZE, SLOT_SIZE);
        }

        drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, SELECTED_TEXTURE, Math.round(currentHudX),
                Math.round(currentAnimationY),
                0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
    }

    public static void setSelectedSlot(int slot) {
        selectedSlot = ((slot % SLOT_COUNT) + SLOT_COUNT) % SLOT_COUNT;
        showHUD();
    }

    public static void showHUD() {
        lastActivityTime = System.currentTimeMillis();
    }

    public static int getSelectedSlot() {
        return selectedSlot;
    }
}