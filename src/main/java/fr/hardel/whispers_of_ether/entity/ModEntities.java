package fr.hardel.whispers_of_ether.entity;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
        public static final EntityType<TargetDummy> TARGET_DUMMY = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "target_dummy"),
                EntityType.Builder.of(TargetDummy::new, MobCategory.MISC)
                        .sized(1.0f, 2.0f)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID,
                                        "target_dummy"))));

        public static final EntityType<DamageIndicator> DAMAGE_INDICATOR = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "damage_indicator"),
                EntityType.Builder.of(DamageIndicator::new, MobCategory.MISC)
                        .sized(0.0f, 0.0f)
                        .clientTrackingRange(10)
                        .updateInterval(Integer.MAX_VALUE)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID,
                                        "damage_indicator"))));

        public static void register() {
                FabricDefaultAttributeRegistry.register(TARGET_DUMMY, TargetDummy.createAttributes());
        }
}
