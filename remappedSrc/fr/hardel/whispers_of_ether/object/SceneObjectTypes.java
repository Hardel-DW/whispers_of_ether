package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class SceneObjectTypes {
    public static final SceneObjectType GALAXY_SPHERE = register("galaxy_sphere");
    public static final SceneObjectType SINGULAIRY = register("singularity");

    private static SceneObjectType register(String name) {
        return Registry.register(SceneObjectType.REGISTRY,
                Identifier.of(WhispersOfEther.MOD_ID, name), new SceneObjectType());
    }

    public static Text getDisplayName(SceneObjectType type) {
        var id = SceneObjectType.REGISTRY.getId(type);
        return id != null
                ? Text.translatable("scene_object.%s.%s".formatted(id.getNamespace(), id.getPath()))
                : Text.literal("unknown");
    }

    public static String getTranslationKey(SceneObjectType type) {
        var id = SceneObjectType.REGISTRY.getId(type);
        return id != null
                ? "scene_object.%s.%s".formatted(id.getNamespace(), id.getPath())
                : "scene_object.unknown";
    }

    public static void register() {
    }
}