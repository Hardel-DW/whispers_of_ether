package fr.hardel.whispers_of_ether.client.render.pipeline;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.client.render.obj.Sphere;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.renderer.RenderPipelines;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class EtherSphereRenderer implements SceneObjectRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtherSphereRenderer.class);

    private static final Identifier NOISE_TEXTURE = Identifier.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/shader/noise.png");
    private static final Identifier STARS_TEXTURE = Identifier.fromNamespaceAndPath(
        WhispersOfEther.MOD_ID,
        "textures/shader/stars.png");

    private final RenderType renderLayer;

    public EtherSphereRenderer() {
        this.renderLayer = RenderType.create(
            "ether_galaxy",
            RenderSetup.builder(createGalaxyPipeline())
                .withTexture("Sampler0", NOISE_TEXTURE)
                .withTexture("Sampler1", STARS_TEXTURE)
                .useLightmap()
                .useOverlay()
                .createRenderSetup());
    }

    private RenderPipeline createGalaxyPipeline() {
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
                .withLocation(Identifier.parse("rendertype_galaxy"))
                .withVertexShader(Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID,
                    "core/galaxy"))
                .withFragmentShader(Identifier
                    .fromNamespaceAndPath(WhispersOfEther.MOD_ID, "core/galaxy"))
                .withSampler("Sampler0")
                .withSampler("Sampler1")
                .withVertexFormat(DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS)
                .withBlend(BlendFunction.TRANSLUCENT)
                .withCull(false)
                .build();
        } catch (Exception e) {
            LOGGER.error("Error creating galaxy pipeline: {}", e.getMessage(), e);
            return RenderPipelines.ENTITY_TRANSLUCENT;
        }
    }

    @Override
    public SceneObjectType getType() {
        return SceneObjectTypes.GALAXY_SPHERE;
    }

    @Override
    public void render(PoseStack matrices, WorldRenderContext context, SceneObject object) {
        matrices.pushPose();
        matrices.translate(object.position().x, object.position().y, object.position().z);

        VertexConsumer vertexConsumer = Objects.requireNonNull(context.consumers()).getBuffer(renderLayer);
        new Sphere(object.radius()).render(vertexConsumer, matrices);

        matrices.popPose();
    }
}
