package fr.hardel.whispers_of_ether.client.item;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record RuneTier() implements RangeSelectItemModelProperty {
    public static final MapCodec<RuneTier> MAP_CODEC = MapCodec.unit(new RuneTier());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return itemStack.getOrDefault(ModItemComponent.RUNES, RuneComponent.EMPTY).tier();
    }

    @Override
    public @NotNull MapCodec<RuneTier> type() {
        return MAP_CODEC;
    }
}
