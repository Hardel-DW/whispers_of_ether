package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record SummonAction(ResourceLocation entity) implements Action {

    public static final MapCodec<SummonAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("entity").forGetter(SummonAction::entity))
            .apply(instance, SummonAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.SUMMON;
    }

    @Override
    public void execute(ServerLevel world, Entity caster, List<Entity> targets) {
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entity);

        if (entityType == null) {
            return;
        }

        for (Entity target : targets) {
            Vec3 spawnPos = target.position();
            Entity summonedEntity = entityType.create(world);

            if (summonedEntity != null) {
                summonedEntity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                world.addFreshEntity(summonedEntity);
            }
        }
    }
}
