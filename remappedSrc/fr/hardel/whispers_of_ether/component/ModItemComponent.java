package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItemComponent {

    public static final ComponentType<Integer> RUNES = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(WhispersOfEther.MOD_ID, "rune"),
            ComponentType.<Integer>builder().codec(Codec.INT).build());

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering {} components", WhispersOfEther.MOD_ID);
    }

}