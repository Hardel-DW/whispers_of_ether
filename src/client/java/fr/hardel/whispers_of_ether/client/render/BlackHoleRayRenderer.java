package fr.hardel.whispers_of_ether.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.object.SceneObject;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class BlackHoleRayRenderer {
    private static RenderPipeline pipeline;
    private static final Identifier NOISE_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/noise_colored.png");
    private static final Identifier STARS_TEXTURE = Identifier.of(WhispersOfEther.MOD_ID,
            "textures/shader/stars.png");

    private static RenderPipeline getPipeline() {
        if (pipeline == null) {
            RenderPipeline.Snippet transforms = RenderPipeline.builder()
                    .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
                    .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();
            RenderPipeline.Snippet fog = RenderPipeline.builder()
                    .withUniform("Fog", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();
            RenderPipeline.Snippet globals = RenderPipeline.builder()
                    .withUniform("Globals", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();

            pipeline = RenderPipeline.builder(transforms, fog, globals)
                    .withLocation(Identifier.of("rendertype_blackhole_ray"))
                    .withVertexShader(Identifier.of(WhispersOfEther.MOD_ID, "core/blackhole_ray"))
                    .withFragmentShader(Identifier.of(WhispersOfEther.MOD_ID, "core/blackhole_ray"))
                    .withSampler("Sampler0")
                    .withSampler("Sampler1")
                    .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                            VertexFormat.DrawMode.QUADS)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build();
        }
        return pipeline;
    }

    private static final RenderLayer LAYER = RenderLayer.of(
            "ether_blackhole_ray",
            1536,
            false,
            false,
            getPipeline(),
            RenderLayer.MultiPhaseParameters.builder()
                    .texture(RenderPhase.Textures.create()
                            .add(NOISE_TEXTURE, false)
                            .add(STARS_TEXTURE, false)
                            .build())
                    .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                    .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                    .build(false));

    public void render(MatrixStack matrices, WorldRenderContext ctx, SceneObject obj) {
        Vec3d p = obj.position();
        float r = obj.radius();
        matrices.push();
        matrices.translate(p.x, p.y, p.z);

        var client = net.minecraft.client.MinecraftClient.getInstance();
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y
                .rotationDegrees(-client.gameRenderer.getCamera().getYaw()));
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_X
                .rotationDegrees(client.gameRenderer.getCamera().getPitch()));

        VertexConsumerProvider vcp = ctx.consumers();
        assert vcp != null;
        VertexConsumer buf = vcp.getBuffer(LAYER);

        var m = matrices.peek().getPositionMatrix();

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
