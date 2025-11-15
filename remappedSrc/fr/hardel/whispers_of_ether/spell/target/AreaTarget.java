package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.PositionTarget;
import fr.hardel.whispers_of_ether.spell.target.position.RelativePosition;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import fr.hardel.whispers_of_ether.spell.target.shape.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record AreaTarget(List<Shape> shapes, PositionTarget position, Optional<Integer> limit,
        Optional<LootCondition> condition) implements Target {

    public static final MapCodec<AreaTarget> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Shape.CODEC.listOf().fieldOf("shapes").forGetter(AreaTarget::shapes),
            PositionTarget.CODEC.optionalFieldOf("position", new RelativePosition(Position.ZERO)).forGetter(AreaTarget::position),
            Codec.INT.optionalFieldOf("limit").forGetter(AreaTarget::limit),
            LootCondition.CODEC.optionalFieldOf("requirement").forGetter(AreaTarget::condition))
            .apply(instance, AreaTarget::new));

    @Override
    public TargetType<?> getType() {
        return TargetType.AREA;
    }

    @Override
    public List<Entity> resolve(ServerWorld world, Entity caster) {
        Vec3d centerPos = position.toVec3d(caster);
        List<Entity> foundEntities = new ArrayList<>();

        for (Shape shape : shapes) {
            Box searchBox = shape.getBoundingBox(centerPos);
            List<Entity> entities = world.getOtherEntities(caster, searchBox);

            for (Entity entity : entities) {
                if (foundEntities.contains(entity))
                    continue;

                if (shape.contains(centerPos, entity.getEntityPos())) {
                    if (condition.isPresent()) {
                        LootWorldContext lootWorldContext = new LootWorldContext.Builder(world)
                                .add(LootContextParameters.THIS_ENTITY, entity)
                                .add(LootContextParameters.ORIGIN, entity.getEntityPos())
                                .build(LootContextTypes.COMMAND);
                        LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());

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