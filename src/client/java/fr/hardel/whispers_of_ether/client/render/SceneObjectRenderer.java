package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponent;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public final class SceneObjectRenderer {
    private static final SceneObjectRenderer INSTANCE = new SceneObjectRenderer();

    public static void renderAll(WorldRenderContext context) {
        var client = MinecraftClient.getInstance();
        if (client.world == null) return;

        MatrixStack matrices = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(client.world);
        if (comp != null) {
            comp.getAll().forEach(obj -> INSTANCE.render(obj, context));
        }

        matrices.pop();
    }

    public void render(SceneObject obj, WorldRenderContext ctx) {
        MatrixStack matrices = ctx.matrixStack();
        assert matrices != null : "MatrixStack cannot be null";
        
        var renderer = SceneObjectRendererRegistry.getRenderer(obj.type());
        if (renderer != null) {
            renderer.render(matrices, ctx, obj);
        }
    }
}
