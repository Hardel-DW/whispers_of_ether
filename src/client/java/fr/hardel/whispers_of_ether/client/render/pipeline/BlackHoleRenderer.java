package fr.hardel.whispers_of_ether.client.render.pipeline;

import fr.hardel.whispers_of_ether.client.render.PipelineFactory;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BlackHoleRenderer implements SceneObjectRenderer {
    
    private final RenderLayer renderLayer;

    public BlackHoleRenderer(PipelineFactory pipelineFactory) {
        this.renderLayer = RenderLayer.of(
                "ether_blackhole",
                1536,
                false,
                false,
                pipelineFactory.createBlackHolePipeline(),
                RenderLayer.MultiPhaseParameters.builder()
                        .texture(RenderPhase.Textures.create().build())
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(false));
    }

    @Override
    public SceneObjectType getType() {
        return SceneObjectTypes.BLACK_HOLE;
    }

    @Override
    public void render(MatrixStack matrices, WorldRenderContext context, SceneObject object) {
        matrices.push();
        matrices.translate(object.position().x, object.position().y, object.position().z);

        var client = net.minecraft.client.MinecraftClient.getInstance();
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y
                .rotationDegrees(-client.gameRenderer.getCamera().getYaw()));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X
                .rotationDegrees(client.gameRenderer.getCamera().getPitch()));

        VertexConsumer buf = context.consumers().getBuffer(renderLayer);
        var m = matrices.peek().getPositionMatrix();
        float r = object.radius();

        buf.vertex(m, -r, -r, 0).color(255, 255, 255, 255).texture(0, 1).overlay(0).light(0x00F000F0)
                .normal(matrices.peek(), 0, 0, 1);
        buf.vertex(m, r, -r, 0).color(255, 255, 255, 255).texture(1, 1).overlay(0).light(0x00F000F0)
                .normal(matrices.peek(), 0, 0, 1);
        buf.vertex(m, r, r, 0).color(255, 255, 255, 255).texture(1, 0).overlay(0).light(0x00F000F0)
                .normal(matrices.peek(), 0, 0, 1);
        buf.vertex(m, -r, r, 0).color(255, 255, 255, 255).texture(0, 0).overlay(0).light(0x00F000F0)
                .normal(matrices.peek(), 0, 0, 1);

        matrices.pop();
    }
}
