package fr.hardel.whispers_of_ether.mixin;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> dataComponentType, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag);

    @Inject(method = "addDetailsToTooltip", at = @At(value = "HEAD"))
    private void addAttributeTooltips(Item.TooltipContext context, TooltipDisplay display, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder, CallbackInfo ci) {
        this.addToTooltip(ModItemComponent.RUNES, context, display, builder, tooltipFlag);
        this.addToTooltip(ModItemComponent.WELL, context, display, builder, tooltipFlag);
    }

}
