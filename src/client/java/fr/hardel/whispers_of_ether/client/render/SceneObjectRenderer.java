package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.object.SceneObject;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Objects;

public final class SceneObjectRenderer {
    private final EtherSphereRenderer galaxySphereRenderer = new EtherSphereRenderer();
    private final BlackHoleRenderer blackHoleRenderer = new BlackHoleRenderer();

    public void render(SceneObject obj, WorldRenderContext ctx) {
        MatrixStack matrices = ctx.matrixStack();
        assert matrices != null : "MatrixStack cannot be null";
        switch (obj.type()) {
            case GALAXY_SPHERE ->
                galaxySphereRenderer.render(matrices, Objects.requireNonNull(ctx.consumers()), obj.position(),
                        obj.radius());
            case BLACK_HOLE -> blackHoleRenderer.render(matrices, ctx, obj);
        }
    }
}
