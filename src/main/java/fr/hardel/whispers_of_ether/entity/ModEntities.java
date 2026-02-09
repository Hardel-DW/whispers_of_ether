package fr.hardel.whispers_of_ether.entity;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<TargetDummy> TARGET_DUMMY = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "target_dummy"),
            EntityType.Builder.of(TargetDummy::new, MobCategory.MISC)
                    .sized(1.0f, 2.0f)
                    .build("target_dummy"));

    public static final EntityType<DamageIndicator> DAMAGE_INDICATOR = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "damage_indicator"),
            EntityType.Builder.of(DamageIndicator::new, MobCategory.MISC)
                    .sized(0.0f, 0.0f)
                    .noSave()
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
                    .build("damage_indicator"));

    public static void register() {
        FabricDefaultAttributeRegistry.register(TARGET_DUMMY, TargetDummy.createAttributes());
    }
}
