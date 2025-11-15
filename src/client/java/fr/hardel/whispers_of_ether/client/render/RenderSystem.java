package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

public class RenderSystem {
    private static RenderSystem instance;
    private final Map<SceneObjectType, SceneObjectRenderer> renderers = new HashMap<>();

    private RenderSystem() {
    }

    public static RenderSystem getInstance() {
        if (instance == null) {
            instance = new RenderSystem();
        }
        return instance;
    }

    public static void register() {
        var rendererFactory = new RendererFactory();
        rendererFactory.createRenderSystem();
    }

    public void registerRenderer(SceneObjectRenderer renderer) {
        renderers.put(renderer.getType(), renderer);
    }

    public void renderAll(net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext context) {
        var client = Minecraft.getInstance();
        if (client.level == null)
            return;

        PoseStack matrices = context.matrices();
        var cameraPos = context.gameRenderer().getMainCamera().getPosition();

        matrices.pushPose();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(client.level);
        comp.getAll().forEach(obj -> {
            var renderer = renderers.get(obj.type());
            if (renderer != null) {
                renderer.render(matrices, context, obj);
            }
        });

        matrices.popPose();
    }

    public void cleanup() {
        renderers.clear();
    }
}