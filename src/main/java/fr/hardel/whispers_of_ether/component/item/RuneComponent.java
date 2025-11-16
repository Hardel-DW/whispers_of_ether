package fr.hardel.whispers_of_ether.component.item;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

import net.minecraft.network.codec.ByteBufCodecs;

public record RuneComponent(int count) implements TooltipProvider {
    public static final RuneComponent EMPTY = new RuneComponent(1);
    public static final Codec<RuneComponent> CODEC = Codec.INT.xmap(RuneComponent::new, RuneComponent::count);
    public static final StreamCodec<ByteBuf, RuneComponent> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(RuneComponent::new,
            RuneComponent::count);

    @Override
    public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer,
            final TooltipFlag flag, final DataComponentGetter components) {
        consumer.accept(Component.translatable("component.whispers_of_ether.rune.lore", this.count)
                .withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
