package fr.hardel.whispers_of_ether.client.screen;

import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.waypoint.Waypoint;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.network.chat.Component;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class WaypointRenderer {
    // Distance settings
    private static final double MIN_DISTANCE = 3.0;
    private static final double FADE_START_DISTANCE = 4.0;
    private static final double MAX_DISTANCE = 200.0;

    // Size settings
    private static final float MAX_SIZE = 1.5f;
    private static final float MIN_SIZE = 0.6f;
    private static final float DIAMOND_SIZE = 0.3f;

    // Alpha values
    private static final float DIAMOND_ALPHA = 1f;
    private static final float PING_ALPHA = 1f;

    // Border settings
    private static final float BORDER_THICKNESS = 0.1f;
    private static final float BORDER_SIZE = 0.55f;

    // Text settings
    private static final float TEXT_Y_OFFSET = 1f;
    private static final float TEXT_SCALE = -0.035f;
    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int ALPHA_MAX = 255;

    // Animation settings
    private static final float TIME_MULTIPLIER = 0.1f;
    private static final float PULSE_INTERVAL = 5.0f;
    private static final float PULSE_DURATION = 1.0f;
    private static final float PULSE_SCALE_MAX = 2.15f;

    // Light value
    private static final int LIGHT_VALUE = 15728880;

    public static void render(Vec3 cameraPos, Camera camera, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null)
            return;

        var waypointComponent = ModComponents.WAYPOINTS.get(client.player);
        var waypoints = waypointComponent.getWaypoints();
        if (waypoints.isEmpty())
            return;

        PoseStack matrices = new PoseStack();
        matrices.pushPose();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        for (Waypoint waypoint : waypoints) {
            renderWaypoint(matrices, waypoint, cameraPos, tickDelta);
        }

        matrices.popPose();
    }

    private static void renderWaypoint(PoseStack matrices, Waypoint waypoint, Vec3 cameraPos, float tickDelta) {
        double distance = waypoint.getDistanceTo(cameraPos);

        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE)
            return;

        float alpha = calculateAlpha(distance);
        if (alpha <= 0.0f)
            return;

        Vec3 waypointPos = waypoint.getCenterPosition();
        matrices.pushPose();
        matrices.translate(waypointPos.x, waypointPos.y, waypointPos.z);

        Minecraft client = Minecraft.getInstance();
        matrices.mulPose(Axis.YP.rotationDegrees(-client.gameRenderer.getMainCamera().yRot()));
        matrices.mulPose(Axis.XP.rotationDegrees(client.gameRenderer.getMainCamera().xRot()));

        float scale = calculateScale(distance);
        matrices.scale(scale, scale, scale);

        assert client.level != null;
        float time = (client.level.getGameTime() + tickDelta) * TIME_MULTIPLIER;

        renderDiamond(matrices, waypoint.getColor(), time, alpha);
        renderDistanceText(matrices, distance, alpha);

        matrices.popPose();
    }

    private static float calculateScale(double distance) {
        float normalizedDistance = (float) ((distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE));
        float baseSize = MAX_SIZE - (normalizedDistance * (MAX_SIZE - MIN_SIZE));
        return baseSize * (float) Math.sqrt(distance / 10.0);
    }

    private static float calculateAlpha(double distance) {
        if (distance > FADE_START_DISTANCE) {
            return 1.0f;
        }
        if (distance <= MIN_DISTANCE) {
            return 0.0f;
        }

        float fadeRange = (float) (FADE_START_DISTANCE - MIN_DISTANCE);
        float fadeProgress = (float) (distance - MIN_DISTANCE) / fadeRange;

        return fadeProgress * fadeProgress;
    }

    private static void renderDiamond(PoseStack matrices, int color, float time, float alpha) {
        Matrix4f matrix = matrices.last().pose();

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        var vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        var buffer = vertexConsumers.getBuffer(RenderTypes.textBackgroundSeeThrough());

        // 1. Losange central fixe
        buffer.addVertex(matrix, 0.0f, DIAMOND_SIZE, 0.0f).setColor(r, g, b, DIAMOND_ALPHA * alpha)
                .setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, DIAMOND_SIZE, 0.0f, 0.0f).setColor(r, g, b, DIAMOND_ALPHA * alpha)
                .setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -DIAMOND_SIZE, 0.0f).setColor(r, g, b, DIAMOND_ALPHA * alpha)
                .setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -DIAMOND_SIZE, 0.0f, 0.0f).setColor(r, g, b, DIAMOND_ALPHA * alpha)
                .setLight(LIGHT_VALUE);
        vertexConsumers.endBatch();

        // 2. Bordure statique (avec gap/spacing)
        renderStaticBorder(matrices, r, g, b, alpha);

        // 3. Animation pulse (toutes les 5s)
        renderPulseBorder(matrices, r, g, b, alpha, time);
    }

    private static void renderDistanceText(PoseStack matrices, double distance, float alpha) {
        Minecraft client = Minecraft.getInstance();
        Font textRenderer = client.font;

        Component text = Component.translatable("waypoint.distance", (int) Math.round(distance));

        matrices.pushPose();
        matrices.translate(0, TEXT_Y_OFFSET, 0);
        matrices.scale(TEXT_SCALE, TEXT_SCALE, -TEXT_SCALE);

        int textWidth = textRenderer.width(text);
        Matrix4f matrix = matrices.last().pose();

        int alphaInt = (int) (ALPHA_MAX * alpha);
        int color = (alphaInt << 24) | TEXT_COLOR;

        textRenderer.drawInBatch(text, -textWidth / 2f, 0, color, false, matrix,
                client.renderBuffers().bufferSource(),
                Font.DisplayMode.SEE_THROUGH, 0, LIGHT_VALUE);

        client.renderBuffers().bufferSource().endBatch();
        matrices.popPose();
    }

    private static void renderStaticBorder(PoseStack matrices, float r, float g, float b, float alpha) {
        var vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        var buffer = vertexConsumers.getBuffer(RenderTypes.textBackgroundSeeThrough());
        Matrix4f matrix = matrices.last().pose();

        float outerSize = BORDER_SIZE;
        float innerSize = outerSize - BORDER_THICKNESS;

        // Top-right
        buffer.addVertex(matrix, 0.0f, outerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, outerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, innerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, innerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);

        // Right-bottom
        buffer.addVertex(matrix, outerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -outerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -innerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, innerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);

        // Bottom-left
        buffer.addVertex(matrix, 0.0f, -outerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -outerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -innerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -innerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);

        // Left-top
        buffer.addVertex(matrix, -outerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, outerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, innerSize, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -innerSize, 0.0f, 0.0f).setColor(r, g, b, PING_ALPHA * alpha).setLight(LIGHT_VALUE);

        vertexConsumers.endBatch();
    }

    private static void renderPulseBorder(PoseStack matrices, float r, float g, float b, float alpha, float time) {
        float cycleTime = time % PULSE_INTERVAL;
        if (cycleTime > PULSE_DURATION)
            return;

        float progress = cycleTime / PULSE_DURATION;
        float easedProgress = progress * progress * (3.0f - 2.0f * progress);
        float pulseScale = 1.0f + (easedProgress * (PULSE_SCALE_MAX - 1.0f));
        float pulseAlpha = PING_ALPHA * (1.0f - easedProgress) * alpha;

        if (pulseAlpha <= 0.0f)
            return;

        var vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();
        var buffer = vertexConsumers.getBuffer(RenderTypes.textBackgroundSeeThrough());
        Matrix4f matrix = matrices.last().pose();

        float thickness = BORDER_THICKNESS * pulseScale;
        float outerSize = BORDER_SIZE * pulseScale;
        float innerSize = outerSize - thickness;

        // Top-right
        buffer.addVertex(matrix, 0.0f, outerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, outerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, innerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, innerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);

        // Right-bottom
        buffer.addVertex(matrix, outerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -outerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -innerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, innerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);

        // Bottom-left
        buffer.addVertex(matrix, 0.0f, -outerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -outerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -innerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, -innerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);

        // Left-top
        buffer.addVertex(matrix, -outerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, outerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, 0.0f, innerSize, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);
        buffer.addVertex(matrix, -innerSize, 0.0f, 0.0f).setColor(r, g, b, pulseAlpha).setLight(LIGHT_VALUE);

        vertexConsumers.endBatch();
    }
}