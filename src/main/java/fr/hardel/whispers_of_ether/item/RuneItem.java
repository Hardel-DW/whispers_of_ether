package fr.hardel.whispers_of_ether.item;

import java.util.List;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class RuneItem extends Item {

    public RuneItem(Properties settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        int count = stack.getOrDefault(ModItemComponent.RUNES, 1);
        tooltip.add(Component.translatable("component.whispers_of_ether.rune.lore", count).withStyle(ChatFormatting.GOLD));
    }
}
