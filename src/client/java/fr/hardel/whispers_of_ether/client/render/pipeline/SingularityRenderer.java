package fr.hardel.whispers_of_ether.client.render.pipeline;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.client.render.obj.Circle;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingularityRenderer implements SceneObjectRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingularityRenderer.class);

    private final RenderLayer renderLayer;

    public SingularityRenderer() {
        this.renderLayer = RenderLayer.of(
                "ether_singularity",
                1536,
                false,
                false,
                createSingularityPipeline(),
                RenderLayer.MultiPhaseParameters.builder()
                        .texture(RenderPhase.Textures.create().build())
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(false));
    }

    private RenderPipeline createSingularityPipeline() {
        try {
            var transforms = RenderPipeline.builder()
                    .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
                    .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();
            var fog = RenderPipeline.builder()
                    .withUniform("Fog", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();
            var globals = RenderPipeline.builder()
                    .withUniform("Globals", UniformType.UNIFORM_BUFFER)
                    .buildSnippet();

            return RenderPipeline.builder(transforms, fog, globals)
                    .withLocation(Identifier.of("rendertype_singularity"))
                    .withVertexShader(Identifier.of(WhispersOfEther.MOD_ID, "core/singularity"))
                    .withFragmentShader(Identifier.of(WhispersOfEther.MOD_ID, "core/singularity"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                            VertexFormat.DrawMode.QUADS)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error creating singularity pipeline: {}", e.getMessage(), e);
            return RenderPipelines.ENTITY_TRANSLUCENT;
        }
    }

    @Override
    public SceneObjectType getType() {
        return SceneObjectTypes.SINGULAIRY;
    }

    @Override
    public void render(MatrixStack matrices, WorldRenderContext context, SceneObject object) {
        matrices.push();
        matrices.translate(object.position().x, object.position().y, object.position().z);

        var client = MinecraftClient.getInstance();
        matrices.multiply(RotationAxis.POSITIVE_Y
                .rotationDegrees(-client.gameRenderer.getCamera().getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X
                .rotationDegrees(client.gameRenderer.getCamera().getPitch()));

        VertexConsumer vertexConsumer = context.consumers().getBuffer(renderLayer);
        new Circle(object.radius()).render(vertexConsumer, matrices);

        matrices.pop();
    }
}
