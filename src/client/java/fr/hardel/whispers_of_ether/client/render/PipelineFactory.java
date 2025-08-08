package fr.hardel.whispers_of_ether.client.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PipelineFactory.class);
    
    private final RenderPipeline.Snippet transforms;
    private final RenderPipeline.Snippet fog;
    private final RenderPipeline.Snippet globals;
    
    public PipelineFactory() {
        this.transforms = RenderPipeline.builder()
                .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
                .withUniform("Projection", UniformType.UNIFORM_BUFFER)
                .buildSnippet();
        this.fog = RenderPipeline.builder()
                .withUniform("Fog", UniformType.UNIFORM_BUFFER)
                .buildSnippet();
        this.globals = RenderPipeline.builder()
                .withUniform("Globals", UniformType.UNIFORM_BUFFER)
                .buildSnippet();
    }

    public RenderPipeline createGalaxyPipeline() {
        try {

            return RenderPipeline.builder(transforms, fog, globals)
                    .withLocation(Identifier.of("rendertype_galaxy"))
                    .withVertexShader(Identifier.of(WhispersOfEther.MOD_ID, "core/galaxy"))
                    .withFragmentShader(Identifier.of(WhispersOfEther.MOD_ID, "core/galaxy"))
                    .withSampler("Sampler0")
                    .withSampler("Sampler1")
                    .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                            VertexFormat.DrawMode.QUADS)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error creating galaxy pipeline: {}", e.getMessage(), e);
            return RenderPipelines.ENTITY_TRANSLUCENT;
        }
    }

    public RenderPipeline createBlackHolePipeline() {
        try {
            return RenderPipeline.builder(transforms, fog, globals)
                    .withLocation(Identifier.of("rendertype_blackhole"))
                    .withVertexShader(Identifier.of(WhispersOfEther.MOD_ID, "core/blackhole"))
                    .withFragmentShader(Identifier.of(WhispersOfEther.MOD_ID, "core/blackhole"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                            VertexFormat.DrawMode.QUADS)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .withCull(false)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error creating blackhole pipeline: {}", e.getMessage(), e);
            return RenderPipelines.ENTITY_TRANSLUCENT;
        }
    }
}