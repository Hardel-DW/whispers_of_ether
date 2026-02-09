For more on this, please refer to https://docs.gradle.org/9.2.0/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.
3 actionable tasks: 1 executed, 2 up-to-date
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:18: error: cannot find symbol
import net.minecraft.client.renderer.RenderStateShard;
                                    ^
  symbol:   class RenderStateShard
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:19: error: cannot find symbol
import net.minecraft.client.renderer.RenderType;
                                    ^
  symbol:   class RenderType
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:36: error: cannot find symbol
    private final RenderType renderLayer;
                  ^
  symbol:   class RenderType
  location: class EtherSphereRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:16: error: cannot find symbol
import net.minecraft.client.renderer.RenderType;
                                    ^
  symbol:   class RenderType
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:17: error: cannot find symbol
import net.minecraft.client.renderer.RenderStateShard;
                                    ^
  symbol:   class RenderStateShard
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:30: error: cannot find symbol
    private final RenderType renderLayer;
                  ^
  symbol:   class RenderType
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:10: error: cannot find symbol
import net.minecraft.client.renderer.RenderType;
                                    ^
  symbol:   class RenderType
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:39: error: cannot find symbol
        this.renderLayer = RenderType.create(
                           ^
  symbol:   variable RenderType
  location: class EtherSphereRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:51: error: cannot find symbol
                .setOverlayState(RenderStateShard.OVERLAY)
                                 ^
  symbol:   variable RenderStateShard
  location: class EtherSphereRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:50: error: cannot find symbol
                .setLightmapState(RenderStateShard.LIGHTMAP)
                                  ^
  symbol:   variable RenderStateShard
  location: class EtherSphereRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:45: error: package RenderType does not exist
            RenderType.CompositeState.builder()
                      ^
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:46: error: package RenderStateShard does not exist
                .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                                 ^
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:33: error: cannot find symbol
        this.renderLayer = RenderType.create(
                           ^
  symbol:   variable RenderType
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:43: error: cannot find symbol
                .setOverlayState(RenderStateShard.OVERLAY)
                                 ^
  symbol:   variable RenderStateShard
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:42: error: cannot find symbol
                .setLightmapState(RenderStateShard.LIGHTMAP)
                                  ^
  symbol:   variable RenderStateShard
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:39: error: package RenderType does not exist
            RenderType.CompositeState.builder()
                      ^
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:40: error: package RenderStateShard does not exist
                .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                                 ^
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:89: error: cannot find symbol
            .rotationDegrees(-client.gameRenderer.getMainCamera().getYRot()));
                                                                 ^
  symbol:   method getYRot()
  location: class Camera
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:91: error: cannot find symbol
            .rotationDegrees(client.gameRenderer.getMainCamera().getXRot()));
                                                                ^
  symbol:   method getXRot()
  location: class Camera
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\RenderSystem.java:41: error: cannot find symbol
        var cameraPos = context.gameRenderer().getMainCamera().getPosition();
                                                              ^
  symbol:   method getPosition()
  location: class Camera
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:94: error: cannot find symbol
        matrices.mulPose(Axis.YP.rotationDegrees(-client.gameRenderer.getMainCamera().getYRot()));
                                                                                     ^
  symbol:   method getYRot()
  location: class Camera
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:95: error: cannot find symbol
        matrices.mulPose(Axis.XP.rotationDegrees(client.gameRenderer.getMainCamera().getXRot()));
                                                                                    ^
  symbol:   method getXRot()
  location: class Camera
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:137: error: cannot find symbol
        var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                               ^
  symbol:   variable RenderType
  location: class WaypointRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:183: error: cannot find symbol
        var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                               ^
  symbol:   variable RenderType
  location: class WaypointRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:230: error: cannot find symbol
        var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                               ^
  symbol:   variable RenderType
  location: class WaypointRenderer
25 errors

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileClientJava'.
> Compilation failed; see the compiler output below.
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:18: error: cannot find symbol
  import net.minecraft.client.renderer.RenderStateShard;
                                      ^
    symbol:   class RenderStateShard
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:19: error: cannot find symbol
  import net.minecraft.client.renderer.RenderType;
                                      ^
    symbol:   class RenderType
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:36: error: cannot find symbol
      private final RenderType renderLayer;
                    ^
    symbol:   class RenderType
    location: class EtherSphereRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:16: error: cannot find symbol
  import net.minecraft.client.renderer.RenderType;
                                      ^
    symbol:   class RenderType
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:17: error: cannot find symbol
  import net.minecraft.client.renderer.RenderStateShard;
                                      ^
    symbol:   class RenderStateShard
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:30: error: cannot find symbol
      private final RenderType renderLayer;
                    ^
    symbol:   class RenderType
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:10: error: cannot find symbol
  import net.minecraft.client.renderer.RenderType;
                                      ^
    symbol:   class RenderType
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:39: error: cannot find symbol
          this.renderLayer = RenderType.create(
                             ^
    symbol:   variable RenderType
    location: class EtherSphereRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:51: error: cannot find symbol
                  .setOverlayState(RenderStateShard.OVERLAY)
                                   ^
    symbol:   variable RenderStateShard
    location: class EtherSphereRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:50: error: cannot find symbol
                  .setLightmapState(RenderStateShard.LIGHTMAP)
                                    ^
    symbol:   variable RenderStateShard
    location: class EtherSphereRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:33: error: cannot find symbol
          this.renderLayer = RenderType.create(
                             ^
    symbol:   variable RenderType
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:43: error: cannot find symbol
                  .setOverlayState(RenderStateShard.OVERLAY)
                                   ^
    symbol:   variable RenderStateShard
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:42: error: cannot find symbol
                  .setLightmapState(RenderStateShard.LIGHTMAP)
                                    ^
    symbol:   variable RenderStateShard
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:137: error: cannot find symbol
          var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                                 ^
    symbol:   variable RenderType
    location: class WaypointRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:183: error: cannot find symbol
          var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                                 ^
    symbol:   variable RenderType
    location: class WaypointRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:230: error: cannot find symbol
          var buffer = vertexConsumers.getBuffer(RenderType.textBackgroundSeeThrough());
                                                 ^
    symbol:   variable RenderType
    location: class WaypointRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:89: error: cannot find symbol
              .rotationDegrees(-client.gameRenderer.getMainCamera().getYRot()));
                                                                   ^
    symbol:   method getYRot()
    location: class Camera
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:91: error: cannot find symbol
              .rotationDegrees(client.gameRenderer.getMainCamera().getXRot()));
                                                                  ^
    symbol:   method getXRot()
    location: class Camera
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\RenderSystem.java:41: error: cannot find symbol
          var cameraPos = context.gameRenderer().getMainCamera().getPosition();
                                                                ^
    symbol:   method getPosition()
    location: class Camera
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:94: error: cannot find symbol
          matrices.mulPose(Axis.YP.rotationDegrees(-client.gameRenderer.getMainCamera().getYRot()));
                                                                                       ^
    symbol:   method getYRot()
    location: class Camera
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\screen\WaypointRenderer.java:95: error: cannot find symbol
          matrices.mulPose(Axis.XP.rotationDegrees(client.gameRenderer.getMainCamera().getXRot()));
                                                                                      ^
    symbol:   method getXRot()
    location: class Camera
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:45: error: package RenderType does not exist
              RenderType.CompositeState.builder()
                        ^
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\EtherSphereRenderer.java:46: error: package RenderStateShard does not exist
                  .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                                   ^
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:39: error: package RenderType does not exist
              RenderType.CompositeState.builder()
                        ^
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:40: error: package RenderStateShard does not exist
                  .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                                   ^
  25 errors

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to generate a Build Scan (powered by Develocity).

BUILD FAILED in 1s