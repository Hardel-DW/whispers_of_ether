package fr.hardel.whispers_of_ether.client.gui.screen;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.menu.ForgeMagicMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;

public class ForgeMagicScreen extends AbstractContainerScreen<ForgeMagicMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            WhispersOfEther.MOD_ID,
            "textures/gui/container/forgemagic.png");
    private static final ResourceLocation ATTRIBUTE_LINE = ResourceLocation.fromNamespaceAndPath(
            WhispersOfEther.MOD_ID,
            "textures/gui/sprites/container/forgemagic/attribute_line.png");
    private static final ResourceLocation ATTRIBUTE_ICON_PLACEHOLDER = ResourceLocation.fromNamespaceAndPath(
            WhispersOfEther.MOD_ID,
            "textures/gui/attributes/placeholder.png");

    private static final int STATS_PANEL_WIDTH = 100;
    private static final int STATS_PANEL_HEIGHT = 160;
    private static final int STATS_PANEL_X = 8;
    private static final int STATS_PANEL_Y = 18;
    private static final int ATTRIBUTE_LINE_HEIGHT = 12;
    private static final int ATTRIBUTE_LINE_WIDTH = 73;
    private static final int ATTRIBUTE_ICON_SIZE = 8;
    private static final int LINE_GAP = 1;

    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    public ForgeMagicScreen(ForgeMagicMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 282;
        this.imageHeight = 176;
        this.inventoryLabelX = 111;
        this.inventoryLabelY = 81;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 512, 256);
        renderStatsPanel(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
        renderStatsPanelText(graphics);
    }

    private void renderStatsPanel(GuiGraphics graphics, int x, int y) {
        ItemStack equipmentStack = menu.getEquipmentStack();
        if (equipmentStack.isEmpty()) {
            return;
        }

        List<AttributeEntry> attributes = getAttributeModifiers(equipmentStack);
        if (attributes.isEmpty()) {
            return;
        }

        int panelX = x + STATS_PANEL_X;
        int panelY = y + STATS_PANEL_Y;
        int lineHeight = ATTRIBUTE_LINE_HEIGHT + LINE_GAP;
        int yOffset = -scrollOffset * lineHeight;
        for (int i = 0; i < attributes.size(); i++) {
            int lineY = panelY + yOffset;
            if (lineY >= panelY && lineY < panelY + STATS_PANEL_HEIGHT) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, ATTRIBUTE_LINE, panelX + 14, lineY + 7, 0, 0,
                        ATTRIBUTE_LINE_WIDTH, ATTRIBUTE_LINE_HEIGHT, ATTRIBUTE_LINE_WIDTH, ATTRIBUTE_LINE_HEIGHT);

                int iconX = panelX + 14 + ATTRIBUTE_LINE_WIDTH - ATTRIBUTE_ICON_SIZE - 4;
                int iconY = lineY + 7 + 2;
                graphics.blit(RenderPipelines.GUI_TEXTURED, ATTRIBUTE_ICON_PLACEHOLDER, iconX, iconY, 0, 0,
                        ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE);
            }

            yOffset += lineHeight;
        }

        maxScrollOffset = Math.max(0, (attributes.size() * lineHeight - STATS_PANEL_HEIGHT) / lineHeight);
    }

    private void renderStatsPanelText(GuiGraphics graphics) {
        ItemStack equipmentStack = menu.getEquipmentStack();
        if (equipmentStack.isEmpty()) {
            return;
        }

        List<AttributeEntry> attributes = getAttributeModifiers(equipmentStack);
        if (attributes.isEmpty()) {
            return;
        }

        int lineHeight = ATTRIBUTE_LINE_HEIGHT + LINE_GAP;
        int yOffset = -scrollOffset * lineHeight;
        graphics.pose().pushMatrix();
        graphics.pose().scale(0.5f, 0.5f);

        for (AttributeEntry entry : attributes) {
            int lineY = STATS_PANEL_Y + yOffset;
            String valueText = formatValue(entry.value, entry.operation);
            String attributeName = Component.translatable(entry.name).getString();
            String fullTextStr = valueText + " " + attributeName;
            if (fullTextStr.length() >= 22) {
                fullTextStr = fullTextStr.substring(0, 20) + "...";
            }

            Component fullText = Component.literal(fullTextStr);

            int color = getAttributeColor(entry.attribute, entry.value);
            int textX = (int) ((STATS_PANEL_X + 14 + 2) / 0.5f);
            int centeredY = lineY + 7 + (ATTRIBUTE_LINE_HEIGHT - font.lineHeight / 2) / 2;
            int textY = (int) (centeredY / 0.5f);
            graphics.drawString(font, fullText, textX, textY, color, true);

            yOffset += lineHeight;
        }

        graphics.pose().popMatrix();
    }

    private String formatValue(double value, AttributeModifier.Operation operation) {
        String formatted = switch (operation) {
            case ADD_VALUE -> String.format("%.1f", value);
            case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> String.format("%.0f%%", value * 100);
        };
        return value > 0 ? "+" + formatted : formatted;
    }

    private List<AttributeEntry> getAttributeModifiers(ItemStack stack) {
        List<AttributeEntry> entries = new ArrayList<>();
        ItemAttributeModifiers modifiers = stack.getOrDefault(
                DataComponents.ATTRIBUTE_MODIFIERS,
                ItemAttributeModifiers.EMPTY);

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            entries.add(new AttributeEntry(
                    entry.attribute().value().getDescriptionId(),
                    entry.modifier().amount(),
                    entry.modifier().operation(),
                    entry.attribute().value()));
        }

        return entries;
    }

    private int getAttributeColor(Attribute attribute, double value) {
        ChatFormatting style = attribute.getStyle(value > 0);
        return switch (style) {
            case BLUE -> 0xFF55FF55;
            case RED -> 0xFFFF5555;
            case GRAY -> 0xFFAAAAAA;
            default -> 0xFFFFFFFF;
        };
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int panelX = x + STATS_PANEL_X;
        int panelY = y + STATS_PANEL_Y;

        if (mouseX >= panelX && mouseX < panelX + STATS_PANEL_WIDTH && mouseY >= panelY
                && mouseY < panelY + STATS_PANEL_HEIGHT) {
            scrollOffset = Math.max(0, Math.min(maxScrollOffset, scrollOffset - (int) scrollY));
            return true;
        }

        return false;
    }

    private record AttributeEntry(String name, double value, AttributeModifier.Operation operation,
            Attribute attribute) {
    }
}
