package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public final class SceneObjectTypes {
    public static final SceneObjectType GALAXY_SPHERE = register("galaxy_sphere");
    public static final SceneObjectType SINGULAIRY = register("singularity");

    private static SceneObjectType register(String name) {
        return Registry.register(SceneObjectType.REGISTRY,
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, name), new SceneObjectType());
    }

    public static Component getDisplayName(SceneObjectType type) {
        var id = SceneObjectType.REGISTRY.getKey(type);
        return id != null
            ? Component.translatable("scene_object.%s.%s".formatted(id.getNamespace(), id.getPath()))
            : Component.literal("unknown");
    }

    public static String getTranslationKey(SceneObjectType type) {
        var id = SceneObjectType.REGISTRY.getKey(type);
        return id != null
            ? "scene_object.%s.%s".formatted(id.getNamespace(), id.getPath())
            : "scene_object.unknown";
    }

    public static void register() {}
}