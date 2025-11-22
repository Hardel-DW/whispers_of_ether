package fr.hardel.whispers_of_ether.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.function.Consumer;
import net.minecraft.network.codec.ByteBufCodecs;

public record RuneComponent(ResourceLocation attributeId, AttributeModifier.Operation operation, double weight, int tier, double value) implements TooltipProvider {
    public static final RuneComponent EMPTY = new RuneComponent(
        ResourceLocation.withDefaultNamespace("generic.max_health"),
        AttributeModifier.Operation.ADD_VALUE,
        1.0,
        1,
        1.0
    );

    public static final Codec<RuneComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("attribute_id").forGetter(RuneComponent::attributeId),
        AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(RuneComponent::operation),
        Codec.DOUBLE.fieldOf("weight").forGetter(RuneComponent::weight),
        Codec.INT.fieldOf("tier").forGetter(RuneComponent::tier),
        Codec.DOUBLE.fieldOf("value").forGetter(RuneComponent::value)
    ).apply(instance, RuneComponent::new));

    public static final StreamCodec<ByteBuf, RuneComponent> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, RuneComponent::attributeId,
        AttributeModifier.Operation.STREAM_CODEC, RuneComponent::operation,
        ByteBufCodecs.DOUBLE, RuneComponent::weight,
        ByteBufCodecs.VAR_INT, RuneComponent::tier,
        ByteBufCodecs.DOUBLE, RuneComponent::value,
        RuneComponent::new
    );

    @Override
    public void addToTooltip(final Item.TooltipContext context, final Consumer<Component> consumer,
            final TooltipFlag flag, final DataComponentGetter components) {
        consumer.accept(Component.translatable("component.whispers_of_ether.rune.tier", this.tier)
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        consumer.accept(Component.translatable("component.whispers_of_ether.rune.value", String.format("%.1f", this.value))
                .withStyle(ChatFormatting.GRAY));
    }
}
