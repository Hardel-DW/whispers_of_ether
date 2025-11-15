package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public sealed interface Target 
    permits SelfTarget, AimedTarget, AreaTarget {

    Codec<Target> CODEC = TargetType.REGISTRY.byNameCodec()
        .dispatch("type", Target::getType, TargetType::codec);

    TargetType<?> getType();
    
    List<Entity> resolve(ServerLevel world, Entity caster);
}