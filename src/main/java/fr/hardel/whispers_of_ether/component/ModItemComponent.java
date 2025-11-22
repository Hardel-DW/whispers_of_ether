package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import fr.hardel.whispers_of_ether.component.item.WellComponent;

public class ModItemComponent {

    public static final DataComponentType<RuneComponent> RUNES = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "rune"),
            DataComponentType.<RuneComponent>builder().persistent(RuneComponent.CODEC)
                    .networkSynchronized(RuneComponent.STREAM_CODEC).build());

    public static final DataComponentType<WellComponent> WELL = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "well"),
            DataComponentType.<WellComponent>builder().persistent(WellComponent.CODEC)
                    .networkSynchronized(WellComponent.STREAM_CODEC).build());

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering {} components", WhispersOfEther.MOD_ID);
    }

}