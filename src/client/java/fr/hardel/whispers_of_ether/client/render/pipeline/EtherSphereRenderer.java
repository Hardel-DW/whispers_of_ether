package fr.hardel.whispers_of_ether.client.render.pipeline;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.client.render.obj.Sphere;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class EtherSphereRenderer implements SceneObjectRenderer {
    private static final ShaderProgram GALAXY_SHADER = new ShaderProgram(
            ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "core/galaxy"),
            DefaultVertexFormat.NEW_ENTITY,
            ShaderDefines.EMPTY);

    private static final ResourceLocation NOISE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            WhispersOfEther.MOD_ID,
            "textures/shader/noise.png");
    private static final ResourceLocation STARS_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            WhispersOfEther.MOD_ID,
            "textures/shader/stars.png");

    private final RenderType renderLayer;

    public EtherSphereRenderer() {
        this.renderLayer = RenderType.create(
                "ether_galaxy",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GALAXY_SHADER))
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                .add(NOISE_TEXTURE, false, false)
                                .add(STARS_TEXTURE, false, false)
                                .build())
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setOverlayState(RenderStateShard.OVERLAY)
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false));
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
