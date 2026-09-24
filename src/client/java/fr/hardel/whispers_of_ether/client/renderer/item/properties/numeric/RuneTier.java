package fr.hardel.whispers_of_ether.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.world.item.component.DataComponent;
import fr.hardel.whispers_of_ether.world.item.component.RuneComponent;
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
        return itemStack.getOrDefault(DataComponent.RUNES, RuneComponent.EMPTY).tier();
    }

    @Override
    public @NotNull MapCodec<RuneTier> type() {
        return MAP_CODEC;
    }
}
