package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.gl.RenderPipelines;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.pipeline.BlendFunction;
import net.minecraft.client.gl.UniformType;

public class EtherSphereRenderer {

    private static final Identifier NOISE_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/noise.png");
    private static final Identifier STARS_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/stars.png");
    private static RenderPipeline customPipeline;

    private static RenderPipeline getGalaxyPipeline() {
        if (customPipeline == null) {
            try {
                // COPIE EXACTE de END_PORTAL - snippets dans le même ordre !
                RenderPipeline.Snippet transformsSnippet = RenderPipeline.builder()
                        .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
                        .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                        .buildSnippet();
                RenderPipeline.Snippet fogSnippet = RenderPipeline.builder()
                        .withUniform("Fog", UniformType.UNIFORM_BUFFER)
                        .buildSnippet();
                RenderPipeline.Snippet globalsSnippet = RenderPipeline.builder()
                        .withUniform("Globals", UniformType.UNIFORM_BUFFER)
                        .buildSnippet();

                // Pipeline EXACTEMENT comme END_PORTAL avec snippets dans le même ordre
                customPipeline = RenderPipeline.builder(transformsSnippet, fogSnippet, globalsSnippet)
                        .withLocation(Identifier.of("rendertype_galaxy"))
                        .withVertexShader(Identifier.of("core/galaxy"))
                        .withFragmentShader(Identifier.of("core/galaxy"))
                        .withSampler("Sampler0")
                        .withSampler("Sampler1")
                        .withVertexFormat(
                                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                                VertexFormat.DrawMode.QUADS)
                        .withBlend(BlendFunction.TRANSLUCENT)
                        .withCull(false)
                        .build();
            } catch (Exception e) {
                System.out.println("[EtherSphere] ERREUR création pipeline: " + e.getMessage());
                e.printStackTrace();
                return RenderPipelines.ENTITY_TRANSLUCENT; // Fallback
            }
        }
        return customPipeline;
    }

    private static final RenderLayer GALAXY_LAYER = RenderLayer.of(
            "ether_galaxy",
            1536,
            false,
            false,
            getGalaxyPipeline(),
            RenderLayer.MultiPhaseParameters.builder()
                    .texture(RenderPhase.Textures.create()
                            .add(NOISE_TEXTURE, false)
                            .add(STARS_TEXTURE, false)
                            .build())
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                    .build(false));

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Vec3d center, float radius) {
        matrices.push();
        matrices.translate(center.x, center.y, center.z);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GALAXY_LAYER);
        renderSphere(vertexConsumer, matrices, radius);

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

                // UVs not used for sampling anymore (shader uses spherical mapping via normal)
                float u1 = (float) lon / segments;
                float v1 = (float) lat / segments;
                float u2 = (float) (lon + 1) / segments;
                float v2 = (float) (lat + 1) / segments;

                float nx1 = x1 / radius, ny1 = y1 / radius, nz1 = z1 / radius;
                float nx2 = x2 / radius, ny2 = y2 / radius, nz2 = z2 / radius;
                float nx3 = x3 / radius, ny3 = y3 / radius, nz3 = z3 / radius;
                float nx4 = x4 / radius, ny4 = y4 / radius, nz4 = z4 / radius;

                vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x1, y1, z1)
                        .color(255, 255, 255, 255)
                        .texture(u1, v1)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx1, ny1, nz1);
                vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x2, y2, z2)
                        .color(255, 255, 255, 255)
                        .texture(u2, v1)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx2, ny2, nz2);
                vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x3, y3, z3)
                        .color(255, 255, 255, 255)
                        .texture(u2, v2)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx3, ny3, nz3);
                vertexConsumer.vertex(matrices.peek().getPositionMatrix(), x4, y4, z4)
                        .color(255, 255, 255, 255)
                        .texture(u1, v2)
                        .overlay(0)
                        .light(0x00F000F0)
                        .normal(matrices.peek(), nx4, ny4, nz4);
            }
        }
    }
}