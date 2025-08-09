package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.client.render.pipeline.SingularityRenderer;
import fr.hardel.whispers_of_ether.client.render.pipeline.EtherSphereRenderer;

public class RendererFactory {

    public RenderSystem createRenderSystem() {
        RenderSystem renderSystem = RenderSystem.getInstance();
        registerRenderers(renderSystem);
        return renderSystem;
    }

    private void registerRenderers(RenderSystem renderSystem) {
        renderSystem.registerRenderer(new EtherSphereRenderer());
        renderSystem.registerRenderer(new SingularityRenderer());
    }
}