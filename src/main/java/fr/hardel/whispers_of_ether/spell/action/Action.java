package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public interface Action {
    Codec<Action> CODEC = ActionType.REGISTRY.getCodec()
            .dispatch("type", Action::getType, ActionType::codec);

    ActionType<?> getType();

    void execute(ServerWorld world, Entity caster, List<Entity> targets);
}