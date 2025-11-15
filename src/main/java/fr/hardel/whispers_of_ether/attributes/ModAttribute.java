package fr.hardel.whispers_of_ether.attributes;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public class ModAttribute {
    public static final Holder<Attribute> CAMERA_SHAKING_STRENTH = register("camera_shaking_strength",
            new RangedAttribute("attribute.name.camera_shaking_strength", 0.0, 0.0, 10.0).setSyncable(true));
    public static final Holder<Attribute> CAMERA_SHAKING_FREQUENCY = register("camera_shaking_frequency",
            new RangedAttribute("attribute.name.camera_shaking_frequency", 0.0, 0.0, 10.0).setSyncable(true));

    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, id), attribute);
    }

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering Mod Attributes for {}", WhispersOfEther.MOD_ID);

        FabricDefaultAttributeRegistry.register(EntityType.PLAYER,
                Player.createAttributes().add(CAMERA_SHAKING_STRENTH).add(CAMERA_SHAKING_FREQUENCY));
    }
}