package fr.hardel.whispers_of_ether.client.render.obj;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class Circle {
    private final float radius;

    public Circle(float radius) {
        this.radius = radius;
    }

    public void render(VertexConsumer v, MatrixStack matrices) {
        var m = matrices.peek().getPositionMatrix();
        float[][] verts = {{-radius, -radius, 0, 0, 1}, {radius, -radius, 0, 1, 1}, {radius, radius, 0, 1, 0}, {-radius, radius, 0, 0, 0}};
        for (var vert : verts)
            v.vertex(m, vert[0], vert[1], vert[2]).color(255,255,255,255).texture(vert[3], vert[4]).overlay(0).light(0x00F000F0).normal(matrices.peek(), 0, 0, 1);
    }
}