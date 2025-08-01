package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public record SummonAction(Identifier entity) implements Action {
    
    public static final MapCodec<SummonAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Identifier.CODEC.fieldOf("entity").forGetter(SummonAction::entity)
    ).apply(instance, SummonAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.SUMMON;
    }

    @Override
    public void execute(ServerWorld world, Entity caster, List<Entity> targets) {
        EntityType<?> entityType = Registries.ENTITY_TYPE.get(entity);
        
        if (entityType == null) {
            return;
        }
        
        for (Entity target : targets) {
            Vec3d spawnPos = target.getPos();
            Entity summonedEntity = entityType.create(world, SpawnReason.TRIGGERED);
            
            if (summonedEntity != null) {
                summonedEntity.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
                world.spawnEntity(summonedEntity);
            }
        }
    }
}