package fr.hardel.whispers_of_ether.client.render.entity;

import net.minecraft.client.renderer.entity.DisplayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DamageIndicatorRenderer extends DisplayRenderer.TextDisplayRenderer {
    public DamageIndicatorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
}
