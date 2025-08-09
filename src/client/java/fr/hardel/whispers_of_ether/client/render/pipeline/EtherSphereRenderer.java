package fr.hardel.whispers_of_ether.client.render.pipeline;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.PipelineFactory;
import fr.hardel.whispers_of_ether.client.render.SceneObjectRenderer;
import fr.hardel.whispers_of_ether.client.render.obj.Sphere;
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
        new Sphere(object.radius()).render(vertexConsumer, matrices);

        matrices.pop();
    }
}
