package fr.hardel.whispers_of_ether.component.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

public record WellComponent(double maxWell, double currentWell) {
    public static final WellComponent EMPTY = new WellComponent(300.0, 300.0);

    public static final Codec<WellComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.DOUBLE.fieldOf("max_well").forGetter(WellComponent::maxWell),
        Codec.DOUBLE.fieldOf("current_well").forGetter(WellComponent::currentWell)).apply(instance, WellComponent::new));

    public static final StreamCodec<ByteBuf, WellComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.DOUBLE, WellComponent::maxWell,
        ByteBufCodecs.DOUBLE, WellComponent::currentWell,
        WellComponent::new);

    public WellComponent consume(double amount) {
        return new WellComponent(maxWell, Math.max(0, currentWell - amount));
    }

    public double getWellFactor() {
        return currentWell / maxWell;
    }
}
