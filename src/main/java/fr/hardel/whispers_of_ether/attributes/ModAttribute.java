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
import net.minecraft.resources.Identifier;

public class ModAttribute {
        public static final Holder<Attribute> CAMERA_SHAKING_STRENTH = register("camera_shaking_strength",
                new RangedAttribute("attribute.name.camera_shaking_strength", 0.0, 0.0, 10.0)
                        .setSyncable(true));
        public static final Holder<Attribute> CAMERA_SHAKING_FREQUENCY = register("camera_shaking_frequency",
                new RangedAttribute("attribute.name.camera_shaking_frequency", 0.0, 0.0, 10.0)
                        .setSyncable(true));
        public static final Holder<Attribute> PICKUP_AREA_SIZE = register("pickup_area_size",
                new RangedAttribute("attribute.name.pickup_area_size", 1.0, 0.0,
                        10.0).setSyncable(true));
        public static final Holder<Attribute> MULTI_JUMP = register("multi_jump",
                new RangedAttribute("attribute.name.multi_jump", 1, 0,
                        Integer.MAX_VALUE).setSyncable(true));
        public static final Holder<Attribute> CRIT_RATE = register("crit_rate",
                new RangedAttribute("attribute.name.crit_rate", 1.0, 0.0, 1024.0).setSyncable(true));
        public static final Holder<Attribute> CRIT_DAMAGE = register("crit_damage",
                new RangedAttribute("attribute.name.crit_damage", 1.0, 0.0, 1024.0).setSyncable(true));
        public static final Holder<Attribute> OMNIVAMPIRISM = register("omnivampirism",
                new RangedAttribute("attribute.name.omnivampirism", 1.0, 0.0, 10.0).setSyncable(true));
        public static final Holder<Attribute> OMNIVAMPIRISM_RATE = register("omnivampirism_rate",
                new RangedAttribute("attribute.name.omnivampirism_rate", 1.0, 0.0, 1024.0).setSyncable(true));

        private static Holder<Attribute> register(String id, Attribute attribute) {
                return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
                        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, id), attribute);
        }

        public static void register() {
                WhispersOfEther.LOGGER.info("Registering Mod Attributes for {}", WhispersOfEther.MOD_ID);

                FabricDefaultAttributeRegistry.register(EntityType.PLAYER,
                        Player.createAttributes().add(CAMERA_SHAKING_STRENTH).add(CAMERA_SHAKING_FREQUENCY)
                                .add(PICKUP_AREA_SIZE).add(MULTI_JUMP).add(CRIT_RATE).add(CRIT_DAMAGE)
                                .add(OMNIVAMPIRISM).add(OMNIVAMPIRISM_RATE));
        }
}

/**
 * For omnivampirism: /give @a
 * minecraft:diamond_sword[minecraft:attribute_modifiers=[{"id":"attack_damage","type":"attack_damage","amount":7,"operation":"add_value"},{"id":"omnivampirism","type":"whispers_of_ether:omnivampirism","amount":1,"operation":"add_multiplied_base"},{"id":"omnivampirism_rate","type":"whispers_of_ether:omnivampirism_rate","amount":1,"operation":"add_multiplied_base"}]]
 * For crit rate: give @a
 * minecraft:diamond_sword[minecraft:attribute_modifiers=[{"id":"attack_damage","type":"attack_damage","amount":7,"operation":"add_value"},{"id":"crit_rate","type":"whispers_of_ether:crit_rate","amount":1,"operation":"add_multiplied_base"},{"id":"crit_damage","type":"whispers_of_ether:crit_damage","amount":1,"operation":"add_multiplied_base"}]]
 */