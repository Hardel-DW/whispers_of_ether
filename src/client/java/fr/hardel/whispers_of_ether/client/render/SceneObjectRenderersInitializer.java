package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import java.util.Objects;

public final class SceneObjectRenderersInitializer {
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;
        initialized = true;

        registerRenderers();
        WorldRenderEvents.AFTER_TRANSLUCENT.register(SceneObjectRenderer::renderAll);
    }

    private static void registerRenderers() {
        var galaxySphereRenderer = new EtherSphereRenderer();
        var blackHoleRenderer = new BlackHoleRenderer();

        SceneObjectRendererRegistry.registerRenderer(
            SceneObjectTypes.GALAXY_SPHERE,
            (matrices, ctx, obj) -> galaxySphereRenderer.render(matrices,
                Objects.requireNonNull(ctx.consumers()), obj.position(), obj.radius())
        );

        SceneObjectRendererRegistry.registerRenderer(
            SceneObjectTypes.BLACK_HOLE,
            (matrices, ctx, obj) -> blackHoleRenderer.render(matrices, ctx, obj)
        );
    }
}