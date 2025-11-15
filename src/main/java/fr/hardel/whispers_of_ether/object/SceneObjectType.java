package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

/**
 * Registry entry for scene object types. Empty payload; identity is the
 * registry key.
 * Server/common side definition only; client renderers are registered
 * separately.
 */
public final class SceneObjectType {
    public static final ResourceKey<Registry<SceneObjectType>> REGISTRY_KEY = ResourceKey
            .createRegistryKey(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "scene_object_type"));

    public static final Registry<SceneObjectType> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY)
            .buildAndRegister();

    public static final Codec<SceneObjectType> CODEC = ResourceLocation.CODEC.comapFlatMap(
            id -> {
                var t = REGISTRY.getValue(id);
                return t != null ? DataResult.success(t) : DataResult.error(() -> "Unknown scene object type: " + id);
            },
            t -> {
                var id = REGISTRY.getKey(t);
                return id != null ? id : ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "unknown");
            });

    public static void register() {
    }
}
