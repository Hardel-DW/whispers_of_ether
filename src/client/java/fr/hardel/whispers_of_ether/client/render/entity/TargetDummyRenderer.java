package fr.hardel.whispers_of_ether.client.render.entity;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.render.entity.state.TargetDummyRenderState;
import fr.hardel.whispers_of_ether.entity.TargetDummy;
import fr.hardel.whispers_of_ether.client.render.entity.model.TargetDummyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TargetDummyRenderer extends LivingEntityRenderer<TargetDummy, TargetDummyRenderState, TargetDummyModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID,
            "textures/entity/target_dummy.png");

    public TargetDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new TargetDummyModel(context.bakeLayer(TargetDummyModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(TargetDummyRenderState state) {
        return TEXTURE;
    }

    @Override
    public @NotNull TargetDummyRenderState createRenderState() {
        return new TargetDummyRenderState();
    }

    @Override
    public void extractRenderState(TargetDummy entity, TargetDummyRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.attackAnimationState.copyFrom(entity.attackAnimationState);
    }

    @Override
    protected boolean shouldShowName(TargetDummy entity, double distanceToCameraSq) {
        return false;
    }
}
