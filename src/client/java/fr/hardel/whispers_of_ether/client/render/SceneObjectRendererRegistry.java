package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.HashMap;
import java.util.Map;

public final class SceneObjectRendererRegistry {
    @FunctionalInterface
    public interface Renderer {
        void render(MatrixStack matrices, WorldRenderContext ctx, SceneObject obj);
    }

    private static final Map<SceneObjectType, Renderer> RENDERERS = new HashMap<>();

    public static void registerRenderer(SceneObjectType type, Renderer renderer) {
        RENDERERS.put(type, renderer);
    }

    public static Renderer getRenderer(SceneObjectType type) {
        return RENDERERS.get(type);
    }

    public static void clear() {
        RENDERERS.clear();
    }
}