package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record BoxShape(float x, float y, float z) implements Shape {

    public static final MapCodec<BoxShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("x").forGetter(BoxShape::x),
            Codec.FLOAT.fieldOf("y").forGetter(BoxShape::y),
            Codec.FLOAT.fieldOf("z").forGetter(BoxShape::z)).apply(instance, BoxShape::new));

    @Override
    public ShapeType<?> getType() {
        return ShapeType.BOX;
    }

    @Override
    public AABB getBoundingBox(Vec3 center) {
        return new AABB(
                center.x - x / 2.0, center.y - y / 2.0, center.z - z / 2.0,
                center.x + x / 2.0, center.y + y / 2.0, center.z + z / 2.0);
    }

    @Override
    public boolean contains(Vec3 center, Vec3 point) {
        return Math.abs(point.x - center.x) <= x / 2.0 &&
               Math.abs(point.y - center.y) <= y / 2.0 &&
               Math.abs(point.z - center.z) <= z / 2.0;
    }
}