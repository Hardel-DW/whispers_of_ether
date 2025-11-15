package fr.hardel.whispers_of_ether.attributes;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModAttribute {
    public static final RegistryEntry<EntityAttribute> CAMERA_SHAKING_STRENTH = register("camera_shaking_strength",
            new ClampedEntityAttribute("attribute.name.camera_shaking_strength", 0.0, 0.0, 10.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> CAMERA_SHAKING_FREQUENCY = register("camera_shaking_frequency",
            new ClampedEntityAttribute("attribute.name.camera_shaking_frequency", 0.0, 0.0, 10.0).setTracked(true));

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(WhispersOfEther.MOD_ID, id), attribute);
    }

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering Mod Attributes for {}", WhispersOfEther.MOD_ID);

        FabricDefaultAttributeRegistry.register(EntityType.PLAYER,
                PlayerEntity.createPlayerAttributes().add(CAMERA_SHAKING_STRENTH).add(CAMERA_SHAKING_FREQUENCY));
    }
}