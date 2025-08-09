package fr.hardel.whispers_of_ether.client.render.obj;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class Sphere {
    private final float radius;
    private final int segments;

    public Sphere(float radius) {
        this(radius, Math.max(32, Math.min(96, (int) (radius * 12))));
    }

    public Sphere(float radius, int segments) {
        this.radius = radius;
        this.segments = segments;
    }

    public void render(VertexConsumer vertexConsumer, MatrixStack matrices) {
        var m = matrices.peek().getPositionMatrix();
        for (int lat = 0; lat < segments; lat++) {
            float theta1 = lat * (float) Math.PI / segments;
            float theta2 = (lat + 1) * (float) Math.PI / segments;
            float v1 = (float) lat / segments;
            float v2 = (float) (lat + 1) / segments;

            for (int lon = 0; lon < segments; lon++) {
                float phi1 = lon * 2.0f * (float) Math.PI / segments;
                float phi2 = (lon + 1) * 2.0f * (float) Math.PI / segments;
                float u1 = (float) lon / segments;
                float u2 = (float) (lon + 1) / segments;

                float[][] verts = {
                        { theta1, phi1, u1, v1 },
                        { theta1, phi2, u2, v1 },
                        { theta2, phi2, u2, v2 },
                        { theta2, phi1, u1, v2 }
                };

                for (float[] vert : verts) {
                    float x = radius * (float) (Math.sin(vert[0]) * Math.cos(vert[1]));
                    float y = radius * (float) Math.cos(vert[0]);
                    float z = radius * (float) (Math.sin(vert[0]) * Math.sin(vert[1]));
                    float nx = x / radius, ny = y / radius, nz = z / radius;
                    vertexConsumer.vertex(m, x, y, z)
                            .color(255, 255, 255, 255)
                            .texture(vert[2], vert[3])
                            .overlay(0)
                            .light(0x00F000F0)
                            .normal(matrices.peek(), nx, ny, nz);
                }
            }
        }
    }
}