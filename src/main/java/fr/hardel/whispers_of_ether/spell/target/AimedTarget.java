package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Optional;

public record AimedTarget(float minDistance, float maxDistance, Optional<LootItemCondition> condition) implements Target {

    public static final MapCodec<AimedTarget> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("min_distance").forGetter(AimedTarget::minDistance),
            Codec.FLOAT.fieldOf("max_distance").forGetter(AimedTarget::maxDistance),
            LootItemCondition.DIRECT_CODEC.optionalFieldOf("condition").forGetter(AimedTarget::condition))
            .apply(instance, AimedTarget::new));

    @Override
    public TargetType<?> getType() {
        return TargetType.AIMED;
    }

    @Override
    public List<Entity> resolve(ServerLevel world, Entity caster) {
        if (!(caster instanceof LivingEntity livingCaster)) {
            return List.of();
        }

        Vec3 start = livingCaster.getEyePosition();
        Vec3 direction = livingCaster.getLookAngle();
        Vec3 end = start.add(direction.scale(maxDistance));
        HitResult blockHit = world.clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                livingCaster));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
        }

        AABB searchBox = new AABB(start, end).inflate(1.0);
        List<Entity> entities = world.getEntities(caster, searchBox);

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            double distance = start.distanceTo(entity.position());
            if (distance < minDistance || distance > maxDistance) {
                continue;
            }

            AABB entityBox = entity.getBoundingBox().inflate(0.3);
            Optional<Vec3> hit = entityBox.clip(start, end);

            if (hit.isPresent() && distance < closestDistance) {
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

                closestEntity = entity;
                closestDistance = distance;
            }
        }

        return closestEntity != null ? List.of(closestEntity) : List.of();
    }
}