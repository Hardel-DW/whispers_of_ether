package fr.hardel.whispers_of_ether.client.mixin;

import fr.hardel.whispers_of_ether.client.screen.WaypointRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Camera;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(
        method = "renderLevel",
        at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;")
    )
    private void renderWaypointsAfterFrameGraph(
            DeltaTracker deltaTracker,
            boolean bl,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f matrix4f,
            Matrix4f matrix4f2,
            CallbackInfo ci
    ) {
        Vec3 cameraPos = camera.getPosition();
        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
        WaypointRenderer.render(cameraPos, camera, tickDelta);
    }
}
