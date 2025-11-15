package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public sealed interface PositionTarget 
    permits AbsolutePosition, RelativePosition, LocalPosition {

    Codec<PositionTarget> CODEC = PositionTargetType.REGISTRY.byNameCodec()
        .dispatch("type", PositionTarget::getType, PositionTargetType::codec);

    PositionTargetType<?> getType();
    
    Vec3 toVec3d(Entity caster);
}