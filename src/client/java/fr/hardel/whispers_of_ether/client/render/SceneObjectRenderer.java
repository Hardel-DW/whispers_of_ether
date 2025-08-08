package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;

public interface SceneObjectRenderer {
    void render(MatrixStack matrices, WorldRenderContext context, SceneObject object);
    SceneObjectType getType();
}