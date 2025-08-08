package fr.hardel.whispers_of_ether.client.render.world;

import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponent;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class SceneObjectsRenderer {
    private static final SceneObjectRenderer RENDERER = new SceneObjectRenderer();

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(SceneObjectsRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        var client = MinecraftClient.getInstance();
        if (client.world == null)
            return;

        MatrixStack matrices = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(client.world);
        if (comp != null) {
            for (var obj : comp.getAll()) {
                RENDERER.render(obj, context);
            }
        }

        matrices.pop();
    }
}
