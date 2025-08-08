package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

/**
 * Registry entry for scene object types. Empty payload; identity is the
 * registry key.
 * Server/common side definition only; client renderers are registered
 * separately.
 */
public final class SceneObjectType {
    public static final RegistryKey<Registry<SceneObjectType>> REGISTRY_KEY = RegistryKey
            .ofRegistry(Identifier.of(WhispersOfEther.MOD_ID, "scene_object_type"));

    public static final Registry<SceneObjectType> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY)
            .buildAndRegister();

    public static final Codec<SceneObjectType> CODEC = Identifier.CODEC.comapFlatMap(
            id -> {
                var t = REGISTRY.get(id);
                return t != null ? DataResult.success(t) : DataResult.error(() -> "Unknown scene object type: " + id);
            },
            t -> {
                var id = REGISTRY.getId(t);
                return id != null ? id : Identifier.of(WhispersOfEther.MOD_ID, "unknown");
            });

    public static final SceneObjectType GALAXY_SPHERE = new SceneObjectType();
    public static final SceneObjectType BLACK_HOLE = new SceneObjectType();

    public static void register() {
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "galaxy_sphere"), GALAXY_SPHERE);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "black_hole"), BLACK_HOLE);
    }
}
