package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record SphereShape(float radius) implements Shape {

    public static final MapCodec<SphereShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("radius").forGetter(SphereShape::radius)).apply(instance, SphereShape::new));

    @Override
    public ShapeType<?> getType() {
        return ShapeType.SPHERE;
    }

    @Override
    public AABB getBoundingBox(Vec3 center) {
        return new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius);
    }

    @Override
    public boolean contains(Vec3 center, Vec3 point) {
        return point.distanceTo(center) <= radius;
    }
}