package fr.hardel.whispers_of_ether.client.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import fr.hardel.whispers_of_ether.client.screen.WaypointRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
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
            GraphicsResourceAllocator resourceAllocator,
            DeltaTracker deltaTracker,
            boolean renderOutline,
            Camera camera,
            Matrix4f modelViewMatrix,
            Matrix4f projectionMatrix,
            Matrix4f projectionMatrixForCulling,
            GpuBufferSlice terrainFog,
            Vector4f fogColor,
            boolean shouldRenderSky,
            CallbackInfo ci
    ) {
        Vec3 cameraPos = camera.position();
        float tickDelta = deltaTracker.getGameTimeDeltaPartialTick(false);
        WaypointRenderer.render(cameraPos, camera, tickDelta);
    }
}
