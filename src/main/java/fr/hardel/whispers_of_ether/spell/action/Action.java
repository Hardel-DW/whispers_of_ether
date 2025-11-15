package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public interface Action {
    Codec<Action> CODEC = ActionType.REGISTRY.byNameCodec()
            .dispatch("type", Action::getType, ActionType::codec);

    ActionType<?> getType();

    void execute(ServerLevel world, Entity caster, List<Entity> targets);
}