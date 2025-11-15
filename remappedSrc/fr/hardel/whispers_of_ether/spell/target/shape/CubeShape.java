package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record CubeShape(float size) implements Shape {

    public static final MapCodec<CubeShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(CubeShape::size)).apply(instance, CubeShape::new));

    @Override
    public ShapeType<?> getType() {
        return ShapeType.CUBE;
    }

    @Override
    public Box getBoundingBox(Vec3d center) {
        double half = size / 2.0;
        return new Box(
                center.x - half, center.y - half, center.z - half,
                center.x + half, center.y + half, center.z + half);
    }

    @Override
    public boolean contains(Vec3d center, Vec3d point) {
        double half = size / 2.0;
        return Math.abs(point.x - center.x) <= half &&
               Math.abs(point.y - center.y) <= half &&
               Math.abs(point.z - center.z) <= half;
    }
}