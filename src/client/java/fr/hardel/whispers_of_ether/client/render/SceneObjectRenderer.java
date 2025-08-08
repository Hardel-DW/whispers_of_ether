package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponent;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

public final class SceneObjectRenderer {
    @FunctionalInterface
    public interface Renderer {
        void render(MatrixStack matrices, WorldRenderContext ctx, SceneObject obj);
    }

    private static final EtherSphereRenderer GALAXY_SPHERE_RENDERER = new EtherSphereRenderer();
    private static final BlackHoleRenderer BLACK_HOLE_RENDERER = new BlackHoleRenderer();

    private static final Map<SceneObjectType, Renderer> RENDERERS = new HashMap<>() {
        {
            put(SceneObjectType.GALAXY_SPHERE,
                    (matrices, ctx, obj) -> GALAXY_SPHERE_RENDERER.render(matrices,
                            Objects.requireNonNull(ctx.consumers()), obj.position(), obj.radius()));
            put(SceneObjectType.BLACK_HOLE,
                    (matrices, ctx, obj) -> BLACK_HOLE_RENDERER.render(matrices, ctx, obj));
        }
    };

    public static void registerRenderer(SceneObjectType type, Renderer renderer) {
        RENDERERS.put(type, renderer);
    }

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(SceneObjectRenderer::renderAll);
    }

    private static void renderAll(WorldRenderContext context) {
        var client = MinecraftClient.getInstance();
        if (client.world == null)
            return;

        MatrixStack matrices = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(client.world);
        if (comp != null)
            comp.getAll().forEach(obj -> RENDERER_INSTANCE.render(obj, context));

        matrices.pop();
    }

    private static final SceneObjectRenderer RENDERER_INSTANCE = new SceneObjectRenderer();

    public void render(SceneObject obj, WorldRenderContext ctx) {
        MatrixStack matrices = ctx.matrixStack();
        assert matrices != null : "MatrixStack cannot be null";
        Renderer renderer = RENDERERS.get(obj.type());
        if (renderer != null) {
            renderer.render(matrices, ctx, obj);
        }
    }
}
