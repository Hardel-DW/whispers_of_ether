package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public record SelfTarget() implements Target {
    
    public static final MapCodec<SelfTarget> CODEC = MapCodec.unit(new SelfTarget());

    @Override
    public TargetType<?> getType() {
        return TargetType.SELF;
    }

    @Override
    public List<Entity> resolve(ServerWorld world, Entity caster) {
        return List.of(caster);
    }
}