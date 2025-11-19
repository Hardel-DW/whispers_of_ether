package fr.hardel.whispers_of_ether.client.render.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class TargetDummyRenderState extends LivingEntityRenderState {
    public final AnimationState attackAnimationState = new AnimationState();

    public TargetDummyRenderState() {
    }
}
