package fr.hardel.whispers_of_ether.client.render.pipeline;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.PipelineFactory;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EtherSphereRenderer implements SceneObjectRenderer {

    private static final Identifier NOISE_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/noise.png");
    private static final Identifier STARS_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/stars.png");
    
    private final RenderLayer renderLayer;

    public EtherSphereRenderer(PipelineFactory pipelineFactory) {
        this.renderLayer = RenderLayer.of(
                "ether_galaxy",
                1536,
                false,
                false,
                pipelineFactory.createGalaxyPipeline(),
                RenderLayer.MultiPhaseParameters.builder()
                        .texture(RenderPhase.Textures.create()
                                .add(NOISE_TEXTURE, false)
                                .add(STARS_TEXTURE, false)
                                .build())
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(false));
    }

    @Override
    public SceneObjectType getType() {
        return SceneObjectTypes.GALAXY_SPHERE;
    }

    @Override
    public void render(MatrixStack matrices, WorldRenderContext context, SceneObject object) {
        matrices.push();
        matrices.translate(object.position().x, object.position().y, object.position().z);

        VertexConsumer vertexConsumer = context.consumers().getBuffer(renderLayer);
        renderSphere(vertexConsumer, matrices, object.radius());

        matrices.pop();
    }

    private void renderSphere(VertexConsumer vertexConsumer, MatrixStack matrices, float radius) {
        int segments = Math.max(32, Math.min(96, (int) (radius * 12)));

        for (int lat = 0; lat < segments; lat++) {
            float theta1 = lat * (float) Math.PI / segments;
            float theta2 = (lat + 1) * (float) Math.PI / segments;

            for (int lon = 0; lon < segments; lon++) {
                float phi1 = lon * 2.0f * (float) Math.PI / segments;
                float phi2 = (lon + 1) * 2.0f * (float) Math.PI / segments;

                float x1 = radius * (float) (Math.sin(theta1) * Math.cos(phi1));
                float y1 = radius * (float) Math.cos(theta1);
                float z1 = radius * (float) (Math.sin(theta1) * Math.sin(phi1));

                float x2 = radius * (float) (Math.sin(theta1) * Math.cos(phi2));
                float y2 = radius * (float) Math.cos(theta1);
                float z2 = radius * (float) (Math.sin(theta1) * Math.sin(phi2));

                float x3 = radius * (float) (Math.sin(theta2) * Math.cos(phi2));
                float y3 = radius * (float) Math.cos(theta2);
                float z3 = radius * (float) (Math.sin(theta2) * Math.sin(phi2));

                float x4 = radius * (float) (Math.sin(theta2) * Math.cos(phi1));
                float y4 = radius * (float) Math.cos(theta2);
                float z4 = radius * (float) (Math.sin(theta2) * Math.sin(phi1));

                float u1 = (float) lon / segments;
                float v1 = (float) lat / segments;
                float u2 = (float) (lon + 1) / segments;
                float v2 = (float) (lat + 1) / segments;

                float nx1 = x1 / radius, ny1 = y1 / radius, nz1 = z1 / radius;
                float nx2 = x2 / radius, ny2 = y2 / radius, nz2 = z2 / radius;
                float nx3 = x3 / radius, ny3 = y3 / radius, nz3 = z3 / radius;
                float nx4 = x4 / radius, ny4 = y4 / radius, nz4 = z4 / radius;

                var m = matrices.peek().getPositionMatrix();
                vertexConsumer.vertex(m, x1, y1, z1)
                        .color(255, 255, 255, 255)
                        .texture(u1, v1)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx1, ny1, nz1);
                vertexConsumer.vertex(m, x2, y2, z2)
                        .color(255, 255, 255, 255)
                        .texture(u2, v1)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx2, ny2, nz2);
                vertexConsumer.vertex(m, x3, y3, z3)
                        .color(255, 255, 255, 255)
                        .texture(u2, v2)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx3, ny3, nz3);
                vertexConsumer.vertex(m, x4, y4, z4)
                        .color(255, 255, 255, 255)
                        .texture(u1, v2)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx4, ny4, nz4);
            }
        }
    }
}
