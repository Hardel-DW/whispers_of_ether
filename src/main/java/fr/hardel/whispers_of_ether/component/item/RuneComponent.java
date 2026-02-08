package fr.hardel.whispers_of_ether.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Consumer;
import net.minecraft.network.codec.ByteBufCodecs;

public record RuneComponent(ResourceLocation runeId, int tier) implements TooltipProvider {
    public static final RuneComponent EMPTY = new RuneComponent(
        ResourceLocation.withDefaultNamespace("empty"),
        1
    );

    public static final Codec<RuneComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("rune_id").forGetter(RuneComponent::runeId),
        Codec.INT.fieldOf("tier").forGetter(RuneComponent::tier)
    ).apply(instance, RuneComponent::new));

    public static final StreamCodec<ByteBuf, RuneComponent> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, RuneComponent::runeId,
        ByteBufCodecs.VAR_INT, RuneComponent::tier,
        RuneComponent::new
    );

    @Override
    public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer,
            final TooltipFlag flag) {
        consumer.accept(Component.translatable("component.whispers_of_ether.rune.tier",
                Component.translatable("enchantment.level." + this.tier))
                .withStyle(ChatFormatting.GRAY));
    }
}
