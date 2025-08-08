package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.client.render.pipeline.BlackHoleRenderer;
import fr.hardel.whispers_of_ether.client.render.pipeline.EtherSphereRenderer;

public class RendererFactory {
    private final PipelineFactory pipelineFactory;

    public RendererFactory(PipelineFactory pipelineFactory) {
        this.pipelineFactory = pipelineFactory;
    }

    public RenderSystem createRenderSystem() {
        RenderSystem renderSystem = new RenderSystem();
        registerRenderers(renderSystem);
        return renderSystem;
    }

    private void registerRenderers(RenderSystem renderSystem) {
        renderSystem.registerRenderer(new EtherSphereRenderer(pipelineFactory));
        renderSystem.registerRenderer(new BlackHoleRenderer(pipelineFactory));
    }
}