package fr.hardel.whispers_of_ether.client.render.pipeline;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.client.render.obj.Circle;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class SingularityRenderer implements SceneObjectRenderer {
    private static ShaderInstance singularityShader;

    private final RenderType renderLayer;

    public static void registerShaders(net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback.RegistrationContext context) throws java.io.IOException {
        context.register(
                ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "core/singularity"),
                DefaultVertexFormat.NEW_ENTITY,
                shader -> singularityShader = shader);
    }

    public SingularityRenderer() {
        this.renderLayer = RenderType.create(
                "ether_singularity",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(() -> singularityShader))
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
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
