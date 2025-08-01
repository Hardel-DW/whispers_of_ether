package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Optional;

public record AimedTarget(float minDistance, float maxDistance, Optional<LootCondition> condition) implements Target {

    public static final MapCodec<AimedTarget> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("min_distance").forGetter(AimedTarget::minDistance),
            Codec.FLOAT.fieldOf("max_distance").forGetter(AimedTarget::maxDistance),
            LootCondition.CODEC.optionalFieldOf("condition").forGetter(AimedTarget::condition))
            .apply(instance, AimedTarget::new));

    @Override
    public TargetType<?> getType() {
        return TargetType.AIMED;
    }

    @Override
    public List<Entity> resolve(ServerWorld world, Entity caster) {
        if (!(caster instanceof LivingEntity livingCaster)) {
            return List.of();
        }

        Vec3d start = livingCaster.getEyePos();
        Vec3d direction = livingCaster.getRotationVector();
        Vec3d end = start.add(direction.multiply(maxDistance));
        HitResult blockHit = world.raycast(new RaycastContext(
                start, end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                livingCaster));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getPos();
        }

        Box searchBox = new Box(start, end).expand(1.0);
        List<Entity> entities = world.getOtherEntities(caster, searchBox);

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            double distance = start.distanceTo(entity.getPos());
            if (distance < minDistance || distance > maxDistance) {
                continue;
            }

            Box entityBox = entity.getBoundingBox().expand(0.3);
            Optional<Vec3d> hit = entityBox.raycast(start, end);

            if (hit.isPresent() && distance < closestDistance) {
                if (condition.isPresent()) {
                    LootWorldContext lootWorldContext = new LootWorldContext.Builder(world)
                            .add(LootContextParameters.THIS_ENTITY, entity)
                            .add(LootContextParameters.ORIGIN, entity.getPos())
                            .build(LootContextTypes.COMMAND);
                    LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());

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