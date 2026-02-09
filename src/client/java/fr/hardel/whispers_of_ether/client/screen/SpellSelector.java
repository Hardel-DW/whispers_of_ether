package fr.hardel.whispers_of_ether.client.screen;

import fr.hardel.whispers_of_ether.client.WhispersOfEtherClient;
import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SpellSelector {
    private static final ResourceLocation SLOT_BACKGROUND = ResourceLocation.fromNamespaceAndPath(WhispersOfEtherClient.MOD_ID,
            "textures/gui/sprites/hud/spell/slot_background.png");
    private static final ResourceLocation SELECTED_TEXTURE = ResourceLocation.fromNamespaceAndPath(WhispersOfEtherClient.MOD_ID,
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

    public static void render(GuiGraphics drawContext, DeltaTracker ignoredTickCounter) {
        List<ResourceLocation> spellIds = getValidSpellIds();
        if (spellIds.isEmpty())
            return;

        int actualSlotCount = spellIds.size();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int totalHeight = (SLOT_SIZE * actualSlotCount) + (GAP * (actualSlotCount - 1));
        int startY = (screenHeight - totalHeight) / 2;
        float targetY = startY + (Math.min(selectedSlot, actualSlotCount - 1) * (SLOT_SIZE + GAP));
        currentAnimationY += (targetY - currentAnimationY) * ANIMATION_SPEED;

        long currentTime = System.currentTimeMillis();
        boolean shouldBeVisible = currentTime - lastActivityTime < VISIBILITY_DURATION;
        float targetHudX = shouldBeVisible ? PADDING_LEFT : -SLOT_SIZE - PADDING_LEFT;
        currentHudX += (targetHudX - currentHudX) * SLIDE_SPEED;

        for (int i = 0; i < actualSlotCount; i++) {
            int x = Math.round(currentHudX);
            int y = startY + (i * (SLOT_SIZE + GAP));
            drawContext.blit(SLOT_BACKGROUND, x, y, 0, 0, SLOT_SIZE, SLOT_SIZE,
                    SLOT_SIZE, SLOT_SIZE);

            var spellId = spellIds.get(i);
            var spell = SpellResourceReloadListener.getSpell(spellId);
            if (spell != null) {
                int iconSize = SLOT_SIZE - 4;
                drawContext.blit(spell.getTextureId(),
                        x + 2, y + 2, 0, 0, iconSize, iconSize, iconSize, iconSize);
            }
        }

        for (int i = 0; i < actualSlotCount; i++) {
            int x = Math.round(currentHudX);
            int y = startY + (i * (SLOT_SIZE + GAP));

            var spellId = spellIds.get(i);
            var spell = SpellResourceReloadListener.getSpell(spellId);
            if (spell != null) {
                assert Minecraft.getInstance().player != null;
                var spellComponent = ModComponents.PLAYER_SPELL.get(Minecraft.getInstance().player);
                long remainingCooldown = spellComponent.getRemainingCooldown(spellId);
                if (remainingCooldown > 0 && spell.cooldown().isPresent()) {
                    float progress = (float) remainingCooldown / (spell.cooldown().get() * 1000f);
                    int iconSize = SLOT_SIZE - 4;

                    int overlayHeight = Math.round(iconSize * progress);
                    drawContext.fill(x + 2, y + 2, x + 2 + iconSize, y + 2 + overlayHeight, 0x80808080);

                    String timeText = String.format("%.1fs", remainingCooldown / 1000.0);
                    var textRenderer = Minecraft.getInstance().font;
                    float scale = 0.6f;

                    drawContext.pose().pushPose();
                    drawContext.pose().translate(x + 2 + iconSize / 2f, y + 2 + iconSize / 2f, 0);
                    drawContext.pose().scale(scale, scale, 1);

                    int textWidth = textRenderer.width(timeText);
                    int textHeight = textRenderer.lineHeight;
                    drawContext.drawString(textRenderer, timeText, -textWidth / 2, -textHeight / 2, 0xFFFFFFFF, true);
                    drawContext.pose().popPose();
                }
            }
        }

        drawContext.blit(SELECTED_TEXTURE, Math.round(currentHudX),
                Math.round(currentAnimationY),
                0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
    }

    private static List<ResourceLocation> getValidSpellIds() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null)
            return List.of();

        return ModComponents.PLAYER_SPELL.get(client.player).getSpellIds().stream()
                .filter(spellId -> SpellResourceReloadListener.getSpell(spellId) != null)
                .limit(SLOT_COUNT)
                .toList();
    }

    public static void setSelectedSlot(int slot) {
        List<ResourceLocation> spellIds = getValidSpellIds();
        if (spellIds.isEmpty())
            return;

        int actualSlotCount = spellIds.size();
        selectedSlot = ((slot % actualSlotCount) + actualSlotCount) % actualSlotCount;
        showHUD();
    }

    public static void showHUD() {
        lastActivityTime = System.currentTimeMillis();
    }

    public static int getSelectedSlot() {
        return selectedSlot;
    }

    public static ResourceLocation getSelectedSpellId() {
        List<ResourceLocation> spellIds = getValidSpellIds();
        if (spellIds.isEmpty() || selectedSlot >= spellIds.size()) {
            return null;
        }
        return spellIds.get(selectedSlot);
    }
}