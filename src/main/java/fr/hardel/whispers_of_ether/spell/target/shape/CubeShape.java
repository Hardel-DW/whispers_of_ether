package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record CubeShape(float size) implements Shape {

    public static final MapCodec<CubeShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("size").forGetter(CubeShape::size)).apply(instance, CubeShape::new));

    @Override
    public ShapeType<?> getType() {
        return ShapeType.CUBE;
    }

    @Override
    public AABB getBoundingBox(Vec3 center) {
        double half = size / 2.0;
        return new AABB(
                center.x - half, center.y - half, center.z - half,
                center.x + half, center.y + half, center.z + half);
    }

    @Override
    public boolean contains(Vec3 center, Vec3 point) {
        double half = size / 2.0;
        return Math.abs(point.x - center.x) <= half &&
               Math.abs(point.y - center.y) <= half &&
               Math.abs(point.z - center.z) <= half;
    }
}