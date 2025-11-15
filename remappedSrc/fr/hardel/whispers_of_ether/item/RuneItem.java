package fr.hardel.whispers_of_ether.item;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import fr.hardel.whispers_of_ether.component.ModItemComponent;

public class RuneItem extends Item {

    public RuneItem(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int count = stack.getOrDefault(ModItemComponent.RUNES, 1);
        tooltip.add(Text.translatable("component.whispers_of_ether.rune.lore", count).formatted(Formatting.GOLD));
    }
}
