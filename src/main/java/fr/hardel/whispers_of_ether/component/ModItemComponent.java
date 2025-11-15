package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModItemComponent {

    public static final DataComponentType<Integer> RUNES = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "rune"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build());

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering {} components", WhispersOfEther.MOD_ID);
    }

}