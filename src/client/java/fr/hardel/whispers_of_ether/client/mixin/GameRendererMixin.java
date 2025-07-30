package fr.hardel.whispers_of_ether.client.mixin;

import fr.hardel.whispers_of_ether.client.CameraShakeManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void onRenderWorldStart(RenderTickCounter tickCounter, CallbackInfo ci) {
        CameraShakeManager.getInstance().update();
    }

    @Inject(method = "bobView", at = @At("TAIL"))
    private void applyCameraShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        CameraShakeManager shakeManager = CameraShakeManager.getInstance();
        
        if (shakeManager.isShaking()) {
            float shakeX = shakeManager.getShakeX();
            float shakeY = shakeManager.getShakeY();
            float shakeZ = shakeManager.getShakeZ();
            
            matrices.translate(shakeX, shakeY, shakeZ);
        }
    }
}