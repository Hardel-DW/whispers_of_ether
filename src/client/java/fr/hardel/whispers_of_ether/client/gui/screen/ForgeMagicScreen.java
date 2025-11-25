package fr.hardel.whispers_of_ether.client.gui.screen;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.WellComponent;
import fr.hardel.whispers_of_ether.menu.ForgeHistoryEntry;
import fr.hardel.whispers_of_ether.menu.ForgeMagicMenu;
import fr.hardel.whispers_of_ether.menu.RuneForgeLogic;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ForgeMagicScreen extends AbstractContainerScreen<ForgeMagicMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/container/forgemagic.png");
    private static final ResourceLocation ATTRIBUTE_LINE = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/sprites/container/forgemagic/attribute_line.png");
    private static final ResourceLocation ATTRIBUTE_ICON_FALLBACK = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/attributes/fallback.png");
    private static final ResourceLocation SCROLL_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/gui/sprites/container/forgemagic/scroll.png");
    private static final ResourceLocation HISTORY_LINE_SPRITE = ResourceLocation.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "container/forgemagic/history_line");

    private static final int HISTORY_PANEL_X = 305;
    private static final int HISTORY_PANEL_Y = 18;
    private static final int HISTORY_PANEL_WIDTH = 113;
    private static final int HISTORY_PANEL_HEIGHT = 138;
    private static final int HISTORY_SCROLL_X = 413;
    private static final int HISTORY_SCROLL_Y_MIN = 24;
    private static final int HISTORY_SCROLL_Y_MAX = 156;
    private static final int HISTORY_LINE_WIDTH = 101;
    private static final int HISTORY_LINE_PADDING_X = 2;
    private static final int HISTORY_LINE_PADDING_Y = 6;
    private static final int HISTORY_ICON_SIZE = 16;
    private static final int HISTORY_OFFSET_X = 5;
    private static final int HISTORY_OFFSET_Y = 7;
    private static final int RETURN_AT_CHAR = 30;
    private static final int STATS_PANEL_X = 9;
    private static final int STATS_PANEL_Y = 17;
    private static final int STATS_PANEL_WIDTH = 88;
    private static final int STATS_PANEL_HEIGHT = 138;
    private static final int SCROLL_X = 97;
    private static final int SCROLL_Y_MIN = 24;
    private static final int SCROLL_Y_MAX = 156;
    private static final int SCROLL_WIDTH = 6;
    private static final int SCROLL_HEIGHT = 46;
    private static final int ATTRIBUTE_LINE_PADDING_X = 4;
    private static final int ATTRIBUTE_LINE_PADDING_Y = 2;
    private static final int ATTRIBUTE_LINE_HEIGHT = 12;
    private static final int ATTRIBUTE_LINE_WIDTH = 73;
    private static final int ATTRIBUTE_ICON_SIZE = 8;
    private static final float TEXT_SCALE = 0.5f;
    private static final int LINE_GAP = 1;
    private static final int INVENTORY_LABEL_X = 268;
    private static final int INVENTORY_LABEL_Y = 81;
    private static final int IMAGE_WIDTH = 439;
    private static final int IMAGE_HEIGHT = 176;
    private static final int ASSET_SIZE_X = 512;
    private static final int ASSET_SIZE_Y = 256;
    private static final int WELL_X = 175;
    private static final int WELL_Y = 78;
    private static final int WELL_SIZE_X = 31;
    private static final int WELL_SIZE_Y = 6;
    private static final int OFFSET_Y = 7;
    private static final int OFFSET_X = 14;
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;
    private int historyScrollOffset = 0;
    private int historyMaxScrollOffset = 0;
    private final List<ForgeHistoryEntry> history = new ArrayList<>();

    public ForgeMagicScreen(ForgeMagicMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = IMAGE_WIDTH;
        this.imageHeight = IMAGE_HEIGHT;
        this.inventoryLabelX = INVENTORY_LABEL_X;
        this.inventoryLabelY = INVENTORY_LABEL_Y;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, ASSET_SIZE_X, ASSET_SIZE_Y);
        renderStatsPanel(graphics, x, y);
        renderHistoryPanel(graphics, x, y);
        renderWellDisplay(graphics, x, y);
    }

    public void addHistoryEntry(ForgeHistoryEntry entry) {
        history.addFirst(entry);
    }

    public void clearHistory() {
        history.clear();
        historyScrollOffset = 0;
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
        graphics.enableScissor(panelX, panelY + OFFSET_Y, panelX + STATS_PANEL_WIDTH, panelY + STATS_PANEL_HEIGHT);

        int yOffset = -scrollOffset * lineHeight;
        for (AttributeEntry entry : attributes) {
            int lineY = panelY + yOffset;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ATTRIBUTE_LINE, panelX + OFFSET_X, lineY + OFFSET_Y, 0, 0,
                ATTRIBUTE_LINE_WIDTH, ATTRIBUTE_LINE_HEIGHT, ATTRIBUTE_LINE_WIDTH, ATTRIBUTE_LINE_HEIGHT);

            ResourceLocation iconLocation = getAttributeIcon(entry.attribute);
            int iconX = panelX + OFFSET_X + ATTRIBUTE_LINE_WIDTH - ATTRIBUTE_ICON_SIZE - ATTRIBUTE_LINE_PADDING_X;
            int iconY = lineY + OFFSET_Y + ATTRIBUTE_LINE_PADDING_Y;
            graphics.blit(RenderPipelines.GUI_TEXTURED, iconLocation, iconX, iconY, 0, 0,
                ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE, ATTRIBUTE_ICON_SIZE);

            String valueText = formatValue(entry.value, entry.operation);
            String attributeName = Component.translatable(entry.name).getString();
            String fullTextStr = (valueText + " " + attributeName).length() >= 21 ? (valueText + " " + attributeName).substring(0, 19) + "..." : valueText + " " + attributeName;
            Component fullText = Component.literal(fullTextStr);

            int color = getAttributeColor(entry.attribute, entry.value);
            int textBaseX = panelX + OFFSET_X + ATTRIBUTE_LINE_PADDING_X;
            int textBaseY = lineY + OFFSET_Y + (ATTRIBUTE_LINE_HEIGHT - (int) (font.lineHeight * TEXT_SCALE)) / 2;
            graphics.pose().pushMatrix();
            graphics.pose().translate(textBaseX, textBaseY);
            graphics.pose().scale(TEXT_SCALE, TEXT_SCALE);
            graphics.drawString(font, fullText, 0, 0, color, true);
            graphics.pose().popMatrix();
            yOffset += lineHeight;
        }

        graphics.disableScissor();

        int totalContentHeight = attributes.size() * lineHeight;
        maxScrollOffset = Math.max(0, (totalContentHeight - STATS_PANEL_HEIGHT + lineHeight - 1) / lineHeight);
        renderScrollBar(graphics, x + SCROLL_X, y, scrollOffset, maxScrollOffset, SCROLL_Y_MIN, SCROLL_Y_MAX);
    }

    private String formatValue(double value, AttributeModifier.Operation operation) {
        String formatted = switch (operation) {
            case ADD_VALUE -> BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
            case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> BigDecimal.valueOf(value * 100).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + "%";
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

    private ResourceLocation getAttributeIcon(Attribute attribute) {
        ResourceLocation attributeId = BuiltInRegistries.ATTRIBUTE.getKey(attribute);
        if (attributeId == null) {
            return ATTRIBUTE_ICON_FALLBACK;
        }

        ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath(
            attributeId.getNamespace(),
            "textures/attributes/" + attributeId.getPath() + ".png");

        assert minecraft != null;
        if (minecraft.getResourceManager().getResource(iconLocation).isPresent()) {
            return iconLocation;
        }

        return ATTRIBUTE_ICON_FALLBACK;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        }

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (handlePanelScroll(mouseX, mouseY, x, y, HISTORY_PANEL_X, HISTORY_PANEL_Y, HISTORY_PANEL_WIDTH,
            HISTORY_PANEL_HEIGHT, HISTORY_SCROLL_X, HISTORY_SCROLL_Y_MIN, HISTORY_SCROLL_Y_MAX, historyMaxScrollOffset)) {
            historyScrollOffset = Math.max(0, Math.min(historyMaxScrollOffset, historyScrollOffset - (int) scrollY * 10));
            return true;
        }

        if (handlePanelScroll(mouseX, mouseY, x, y, STATS_PANEL_X, STATS_PANEL_Y, STATS_PANEL_WIDTH,
            STATS_PANEL_HEIGHT, SCROLL_X, SCROLL_Y_MIN, SCROLL_Y_MAX, maxScrollOffset)) {
            scrollOffset = Math.max(0, Math.min(maxScrollOffset, scrollOffset - (int) scrollY));
            return true;
        }

        return false;
    }

    private void renderHistoryPanel(GuiGraphics graphics, int x, int y) {
        if (history.isEmpty()) {
            return;
        }

        int panelX = x + HISTORY_PANEL_X;
        int panelY = y + HISTORY_PANEL_Y;

        graphics.enableScissor(panelX, panelY + HISTORY_OFFSET_Y, panelX + HISTORY_PANEL_WIDTH, panelY + HISTORY_PANEL_HEIGHT);

        int yOffset = -historyScrollOffset;
        for (ForgeHistoryEntry entry : history) {
            int entryHeight = calculateEntryHeight(entry);
            int lineY = panelY + yOffset;

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HISTORY_LINE_SPRITE,
                panelX + HISTORY_OFFSET_X, lineY + HISTORY_OFFSET_Y,
                HISTORY_LINE_WIDTH, entryHeight);

            int iconX = panelX + HISTORY_OFFSET_X + HISTORY_LINE_PADDING_X;
            int iconY = lineY + HISTORY_OFFSET_Y + (entryHeight - HISTORY_ICON_SIZE) / 2;
            graphics.renderItem(entry.runeStack(), iconX, iconY);

            int textStartX = iconX + HISTORY_ICON_SIZE + 2;
            int outcomeColor = getOutcomeColor(entry.outcome());
            String outcomeText = getOutcomeText(entry.outcome());

            drawScaledText(graphics, outcomeText, textStartX, lineY + HISTORY_OFFSET_Y + 4, outcomeColor);

            int statsY = lineY + HISTORY_OFFSET_Y + HISTORY_LINE_PADDING_Y + (int) (font.lineHeight * TEXT_SCALE) + 1;
            drawScaledStats(graphics, entry.statChanges(), textStartX, statsY);

            yOffset += entryHeight + LINE_GAP;
        }

        graphics.disableScissor();

        int totalContentHeight = history.stream().mapToInt(this::calculateEntryHeight).sum() + (history.size() - 1) * LINE_GAP;
        historyMaxScrollOffset = Math.max(0, totalContentHeight - HISTORY_PANEL_HEIGHT + HISTORY_OFFSET_Y + 2);
        renderScrollBar(graphics, x + HISTORY_SCROLL_X, y, historyScrollOffset, historyMaxScrollOffset, HISTORY_SCROLL_Y_MIN, HISTORY_SCROLL_Y_MAX);
    }

    private int calculateEntryHeight(ForgeHistoryEntry entry) {
        int statsLines = calculateStatsLines(entry.statChanges());
        int scaledLineHeight = (int) (font.lineHeight * TEXT_SCALE);
        return HISTORY_LINE_PADDING_Y * 2 + scaledLineHeight + statsLines * scaledLineHeight;
    }

    private int calculateStatsLines(List<ForgeHistoryEntry.StatChange> changes) {
        int currentLineLength = 0;
        int lines = 1;

        for (int i = 0; i < changes.size(); i++) {
            ForgeHistoryEntry.StatChange change = changes.get(i);
            String value = formatValue(change.delta(), change.operation());
            String name = BuiltInRegistries.ATTRIBUTE.get(ResourceKey.create(Registries.ATTRIBUTE, change.attributeId()))
                .map(holder -> Component.translatable(holder.value().getDescriptionId()).getString())
                .orElse(change.attributeId().getPath());
            String text = value + " " + name;
            if (i < changes.size() - 1) {
                text += ", ";
            }

            if (currentLineLength + text.length() > RETURN_AT_CHAR && currentLineLength > 0) {
                lines++;
                currentLineLength = text.length();
            } else {
                currentLineLength += text.length();
            }
        }

        return lines;
    }

    private int getOutcomeColor(RuneForgeLogic.Outcome outcome) {
        return switch (outcome) {
            case CRITICAL_SUCCESS -> 0xFF55FF55;
            case NEUTRAL_SUCCESS -> 0xFFFFFFFF;
            case CRITICAL_FAILURE -> 0xFFFF5555;
            case BLOCKED -> 0xFFAAAAAA;
        };
    }

    private String getOutcomeText(RuneForgeLogic.Outcome outcome) {
        return switch (outcome) {
            case CRITICAL_SUCCESS -> Component.translatable("forge.whispers_of_ether.critical_success").getString();
            case NEUTRAL_SUCCESS -> Component.translatable("forge.whispers_of_ether.neutral_success").getString();
            case CRITICAL_FAILURE -> Component.translatable("forge.whispers_of_ether.critical_failure").getString();
            case BLOCKED -> Component.translatable("forge.whispers_of_ether.blocked").getString();
        };
    }

    private void drawScaledText(GuiGraphics graphics, String text, int x, int y, int color) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(TEXT_SCALE, TEXT_SCALE);
        graphics.drawString(font, text, 0, 0, color, true);
        graphics.pose().popMatrix();
    }

    private void drawScaledStats(GuiGraphics graphics, List<ForgeHistoryEntry.StatChange> changes, int x, int y) {
        graphics.pose().pushMatrix();
        graphics.pose().translate(x, y);
        graphics.pose().scale(TEXT_SCALE, TEXT_SCALE);

        int xOffset = 0;
        int yOffset = 0;
        int currentLineLength = 0;

        for (int i = 0; i < changes.size(); i++) {
            ForgeHistoryEntry.StatChange change = changes.get(i);
            String value = formatValue(change.delta(), change.operation());
            String name = BuiltInRegistries.ATTRIBUTE.get(ResourceKey.create(Registries.ATTRIBUTE, change.attributeId()))
                .map(holder -> Component.translatable(holder.value().getDescriptionId()).getString())
                .orElse(change.attributeId().getPath());
            String text = value + " " + name;
            if (i < changes.size() - 1) {
                text += ", ";
            }

            if (currentLineLength + text.length() > RETURN_AT_CHAR && currentLineLength > 0) {
                xOffset = 0;
                yOffset += font.lineHeight + LINE_GAP;
                currentLineLength = 0;
            }

            int color = change.isPositiveEffect() ? 0xFF55FF55 : 0xFFFF5555;
            graphics.drawString(font, text, xOffset, yOffset, color, true);
            int textWidth = font.width(text);
            xOffset += textWidth;
            currentLineLength += text.length();
        }

        graphics.pose().popMatrix();
    }

    private void renderScrollBar(GuiGraphics graphics, int scrollX, int baseY, int offset, int maxOffset, int yMin, int yMax) {
        if (maxOffset <= 0) {
            return;
        }
        int scrollTrackHeight = yMax - yMin - SCROLL_HEIGHT;
        int scrollY = yMin + (int) ((float) offset / maxOffset * scrollTrackHeight);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SCROLL_TEXTURE, scrollX, baseY + scrollY, 0, 0,
            SCROLL_WIDTH, SCROLL_HEIGHT, SCROLL_WIDTH, SCROLL_HEIGHT);
    }

    private boolean handlePanelScroll(double mouseX, double mouseY, int baseX, int baseY,
        int panelOffsetX, int panelOffsetY, int panelWidth, int panelHeight,
        int scrollBarOffsetX, int scrollYMin, int scrollYMax, int maxOffset) {
        if (maxOffset <= 0) {
            return false;
        }

        int panelX = baseX + panelOffsetX;
        int panelY = baseY + panelOffsetY;
        int scrollBarX = baseX + scrollBarOffsetX;
        int scrollBarY = baseY + scrollYMin;
        int scrollBarMaxY = baseY + scrollYMax;

        boolean inPanel = mouseX >= panelX && mouseX < panelX + panelWidth
            && mouseY >= panelY && mouseY < panelY + panelHeight;
        boolean inScrollBar = mouseX >= scrollBarX && mouseX < scrollBarX + SCROLL_WIDTH
            && mouseY >= scrollBarY && mouseY < scrollBarMaxY;

        return inPanel || inScrollBar;
    }

    private void renderWellDisplay(GuiGraphics graphics, int x, int y) {
        ItemStack equipmentStack = menu.getEquipmentStack();
        if (equipmentStack.isEmpty()) {
            return;
        }

        WellComponent well = equipmentStack.getOrDefault(ModItemComponent.WELL, WellComponent.EMPTY);
        String wellText = String.format("%.0f/%.0f", well.currentWell(), well.maxWell());

        int wellX = x + WELL_X;
        int wellY = y + WELL_Y;
        int scaledTextWidth = (int) (font.width(wellText) * TEXT_SCALE);
        int scaledTextHeight = (int) (font.lineHeight * TEXT_SCALE);
        int centeredX = wellX + (WELL_SIZE_X - scaledTextWidth) / 2;
        int centeredY = wellY + (WELL_SIZE_Y - scaledTextHeight) / 2;

        graphics.pose().pushMatrix();
        graphics.pose().translate(centeredX, centeredY);
        graphics.pose().scale(TEXT_SCALE, TEXT_SCALE);
        graphics.drawString(font, wellText, 0, 0, 0xFFFFFACA, false);
        graphics.pose().popMatrix();
    }

    private record AttributeEntry(String name, double value, AttributeModifier.Operation operation,
        Attribute attribute) {}
}
