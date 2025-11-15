package fr.hardel.whispers_of_ether.client.render;

import fr.hardel.whispers_of_ether.client.render.pipeline.SingularityRenderer;
import fr.hardel.whispers_of_ether.client.render.pipeline.EtherSphereRenderer;

public class RendererFactory {

    public void createRenderSystem() {
        RenderSystem renderSystem = RenderSystem.getInstance();
        registerRenderers(renderSystem);
    }

    private void registerRenderers(RenderSystem renderSystem) {
        renderSystem.registerRenderer(new EtherSphereRenderer());
        renderSystem.registerRenderer(new SingularityRenderer());
    }
}