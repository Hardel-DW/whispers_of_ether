package fr.hardel.whispers_of_ether.client.render.obj;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public record Circle(float radius) {
    public void render(VertexConsumer v, PoseStack matrices) {
        var m = matrices.last().pose();
        float[][] verts = {{-radius, -radius, 0, 0, 1}, {radius, -radius, 0, 1, 1}, {radius, radius, 0, 1, 0}, {-radius, radius, 0, 0, 0}};
        for (var vert : verts)
            v.addVertex(m, vert[0], vert[1], vert[2]).setColor(255, 255, 255, 255).setUv(vert[3], vert[4]).setOverlay(0).setLight(0x00F000F0).setNormal(matrices.last(), 0, 0, 1);
    }
}