package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record SphereShape(float radius) implements Shape {

    public static final MapCodec<SphereShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("radius").forGetter(SphereShape::radius)).apply(instance, SphereShape::new));

    @Override
    public ShapeType<?> getType() {
        return ShapeType.SPHERE;
    }

    @Override
    public Box getBoundingBox(Vec3d center) {
        return new Box(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius);
    }

    @Override
    public boolean contains(Vec3d center, Vec3d point) {
        return point.distanceTo(center) <= radius;
    }
}