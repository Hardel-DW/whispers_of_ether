package fr.hardel.whispers_of_ether.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EtherSphereManager {

    private static final EtherSphereManager INSTANCE = new EtherSphereManager();
    private final Map<String, EtherSphere> spheres = new ConcurrentHashMap<>();
    private final EtherSphereRenderer renderer = new EtherSphereRenderer();

    public static EtherSphereManager getInstance() {
        return INSTANCE;
    }

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(EtherSphereManager::render);
    }

    public void addSphere(String id, Vec3d position, float radius) {
        spheres.put(id, new EtherSphere(position, radius));
    }

    public void removeSphere(String id) {
        spheres.remove(id);
    }

    public void clearSpheres() {
        spheres.clear();
    }

    private static void render(WorldRenderContext context) {
        EtherSphereManager manager = getInstance();
        if (manager.spheres.isEmpty())
            return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null)
            return;

        MatrixStack matrices = context.matrixStack();
        Vec3d cameraPos = context.camera().getPos();

        matrices.push();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        for (EtherSphere sphere : manager.spheres.values()) {
            manager.renderer.render(matrices, context.consumers(), sphere.position, sphere.radius);
        }

        matrices.pop();
    }

    public void cleanup() {
        spheres.clear();
    }

    public static class EtherSphere {
        public final Vec3d position;
        public final float radius;

        public EtherSphere(Vec3d position, float radius) {
            this.position = position;
            this.radius = radius;
        }
    }
}