package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public sealed interface Shape 
    permits SphereShape, CubeShape, BoxShape {

    Codec<Shape> CODEC = ShapeType.REGISTRY.getCodec()
        .dispatch("type", Shape::getType, ShapeType::codec);

    ShapeType<?> getType();
    
    Box getBoundingBox(Vec3d center);
    
    boolean contains(Vec3d center, Vec3d point);
}