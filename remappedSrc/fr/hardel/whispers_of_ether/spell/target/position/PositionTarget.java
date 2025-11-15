package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public sealed interface PositionTarget 
    permits AbsolutePosition, RelativePosition, LocalPosition {

    Codec<PositionTarget> CODEC = PositionTargetType.REGISTRY.getCodec()
        .dispatch("type", PositionTarget::getType, PositionTargetType::codec);

    PositionTargetType<?> getType();
    
    Vec3d toVec3d(Entity caster);
}