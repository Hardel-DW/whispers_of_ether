package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public record SelfTarget() implements Target {
    
    public static final MapCodec<SelfTarget> CODEC = MapCodec.unit(new SelfTarget());

    @Override
    public TargetType<?> getType() {
        return TargetType.SELF;
    }

    @Override
    public List<Entity> resolve(ServerLevel world, Entity caster) {
        return List.of(caster);
    }
}