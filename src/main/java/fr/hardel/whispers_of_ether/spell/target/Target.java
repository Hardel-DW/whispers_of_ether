package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public sealed interface Target 
    permits SelfTarget, AimedTarget, AreaTarget {

    Codec<Target> CODEC = TargetType.REGISTRY.getCodec()
        .dispatch("type", Target::getType, TargetType::codec);

    TargetType<?> getType();
    
    List<Entity> resolve(ServerWorld world, Entity caster);
}