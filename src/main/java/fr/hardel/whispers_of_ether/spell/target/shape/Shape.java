package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.Codec;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public sealed interface Shape 
    permits SphereShape, CubeShape, BoxShape {

    Codec<Shape> CODEC = ShapeType.REGISTRY.byNameCodec()
        .dispatch("type", Shape::getType, ShapeType::codec);

    ShapeType<?> getType();
    
    AABB getBoundingBox(Vec3 center);
    
    boolean contains(Vec3 center, Vec3 point);
}