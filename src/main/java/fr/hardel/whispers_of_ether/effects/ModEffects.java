package fr.hardel.whispers_of_ether.effects;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class ModEffects {
        public static final Holder.Reference<MobEffect> PICKUP_AREA_ENTRY = registryStatusEffect("pickup_area",
                new PickupAreaEffect(MobEffectCategory.NEUTRAL, 0x00FFFF)
                        .addAttributeModifier(ModAttribute.PICKUP_AREA_SIZE,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "pickup_area_size"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE));

        public static final Holder.Reference<MobEffect> MULTI_JUMP_ENTRY = registryStatusEffect("multi_jump",
                new MultiJumpEffect(MobEffectCategory.NEUTRAL, 0x00FFFF)
                        .addAttributeModifier(ModAttribute.MULTI_JUMP,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "multi_jump"),
                                1,
                                AttributeModifier.Operation.ADD_VALUE));

        public static final Holder.Reference<MobEffect> CAMERA_SHAKING_ENTRY = registryStatusEffect("camera_shaking",
                new ShakingEffect(MobEffectCategory.NEUTRAL, 0x00FFFF)
                        .addAttributeModifier(
                                ModAttribute.CAMERA_SHAKING_FREQUENCY,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "camera_shaking_frequency"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE)
                        .addAttributeModifier(ModAttribute.CAMERA_SHAKING_STRENTH,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "camera_shaking_strength"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE));

        public static Holder.Reference<MobEffect> registryStatusEffect(String id, MobEffect effect) {
                return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, id), effect);
        }

        public static void register() {
                WhispersOfEther.LOGGER.info("Registering Mod Effect for {}", WhispersOfEther.MOD_ID);
        }
}
