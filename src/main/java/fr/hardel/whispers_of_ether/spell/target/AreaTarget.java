package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.PositionTarget;
import fr.hardel.whispers_of_ether.spell.target.position.RelativePosition;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import fr.hardel.whispers_of_ether.spell.target.shape.Shape;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record AreaTarget(List<Shape> shapes, PositionTarget position, Optional<Integer> limit,
        Optional<LootItemCondition> condition) implements Target {

    public static final MapCodec<AreaTarget> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Shape.CODEC.listOf().fieldOf("shapes").forGetter(AreaTarget::shapes),
            PositionTarget.CODEC.optionalFieldOf("position", new RelativePosition(Position.ZERO)).forGetter(AreaTarget::position),
            Codec.INT.optionalFieldOf("limit").forGetter(AreaTarget::limit),
            LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirement").forGetter(AreaTarget::condition))
            .apply(instance, AreaTarget::new));

    @Override
    public TargetType<?> getType() {
        return TargetType.AREA;
    }

    @Override
    public List<Entity> resolve(ServerLevel world, Entity caster) {
        Vec3 centerPos = position.toVec3d(caster);
        List<Entity> foundEntities = new ArrayList<>();

        for (Shape shape : shapes) {
            AABB searchBox = shape.getBoundingBox(centerPos);
            List<Entity> entities = world.getEntities(caster, searchBox);

            for (Entity entity : entities) {
                if (foundEntities.contains(entity))
                    continue;

                if (shape.contains(centerPos, entity.position())) {
                    if (condition.isPresent()) {
                        LootParams lootWorldContext = new LootParams.Builder(world)
                                .withParameter(LootContextParams.THIS_ENTITY, entity)
                                .withParameter(LootContextParams.ORIGIN, entity.position())
                                .create(LootContextParamSets.COMMAND);
                        LootContext lootContext = new LootContext.Builder(lootWorldContext).create(Optional.empty());

                        if (!condition.get().test(lootContext)) {
                            continue;
                        }
                    }

                    foundEntities.add(entity);
                }
            }
        }

        if (limit.isPresent() && foundEntities.size() > limit.get()) {
            foundEntities = foundEntities.subList(0, limit.get());
        }

        return foundEntities;
    }

}