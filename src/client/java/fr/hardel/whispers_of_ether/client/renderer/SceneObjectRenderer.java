package fr.hardel.whispers_of_ether.client.gui.render;

import fr.hardel.whispers_of_ether.world.object.SceneObject;
import fr.hardel.whispers_of_ether.world.object.SceneObjectType;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import com.mojang.blaze3d.vertex.PoseStack;

public interface SceneObjectRenderer {
    void render(PoseStack matrices, WorldRenderContext context, SceneObject object);

    SceneObjectType getType();
}