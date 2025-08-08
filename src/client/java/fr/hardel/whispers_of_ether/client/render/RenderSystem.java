package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;

import java.util.HashMap;
import java.util.Map;

public class RenderSystem {
    private static RenderSystem instance;
    private final Map<SceneObjectType, SceneObjectRenderer> renderers = new HashMap<>();

    private RenderSystem() {}

    public static RenderSystem getInstance() {
        if (instance == null) {
            instance = new RenderSystem();
        }
        return instance;
    }

    public static void register() {
        getInstance();
    }

    public void registerRenderer(SceneObjectRenderer renderer) {
        renderers.put(renderer.getType(), renderer);
    }

    public void renderAll(WorldRenderContext context) {
        var client = MinecraftClient.getInstance();
        if (client.world == null)
            return;

        var matrices = context.matrixStack();
        var cameraPos = context.camera().getPos();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(client.world);
        comp.getAll().forEach(obj -> renderers.get(obj.type()).render(matrices, context, obj));

        matrices.pop();
    }

    public void cleanup() {
        renderers.clear();
    }
}