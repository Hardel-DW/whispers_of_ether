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
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.shaders.UniformType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingularityRenderer implements SceneObjectRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingularityRenderer.class);

    private final RenderType renderLayer;

    public SingularityRenderer() {
        this.renderLayer = RenderType.create(
                "ether_singularity",
                1536,
                false,
                false,
                createSingularityPipeline(),
                RenderType.CompositeState.builder()
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                .build())
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .createCompositeState(false));
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
                    .withLocation(ResourceLocation.parse("rendertype_singularity"))
                    .withVertexShader(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID,
                            "core/singularity"))
                    .withFragmentShader(ResourceLocation.fromNamespaceAndPath(
                            WhispersOfEther.MOD_ID, "core/singularity"))
                    .withVertexFormat(DefaultVertexFormat.NEW_ENTITY,
                            VertexFormat.Mode.QUADS)
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
    public void render(PoseStack matrices, WorldRenderContext context, SceneObject object) {
        matrices.pushPose();
        matrices.translate(object.position().x, object.position().y, object.position().z);

        var client = Minecraft.getInstance();
        matrices.mulPose(Axis.YP
                .rotationDegrees(-client.gameRenderer.getMainCamera().getYRot()));
        matrices.mulPose(Axis.XP
                .rotationDegrees(client.gameRenderer.getMainCamera().getXRot()));

        VertexConsumer vertexConsumer = context.consumers().getBuffer(renderLayer);
        new Circle(object.radius()).render(vertexConsumer, matrices);

        matrices.popPose();
    }
}
