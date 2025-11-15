package fr.hardel.whispers_of_ether.spell.timeline;

import fr.hardel.whispers_of_ether.spell.SpellActionExecutor;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimelineScheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerLevel world,
                                                               Entity caster, int delayTicks) {
        return executeTimelineAsync(timeline, world, caster, delayTicks, null);
    }

    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerLevel world,
                                                               Entity caster, int delayTicks, Position offset) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Exécution synchrone sur le thread principal
        Runnable executeTask = () -> {
            try {
                if (timeline.condition().isPresent() && !checkCondition(timeline.condition().get(), world, caster)) {
                    future.complete(null);
                    return;
                }

                Entity effectiveCaster = caster;
                if (offset != null) {
                    double originalX = caster.getX();
                    double originalY = caster.getY();
                    double originalZ = caster.getZ();

                    caster.setPos(
                            originalX + offset.x(),
                            originalY + offset.y(),
                            originalZ + offset.z());

                    for (var action : timeline.actions()) {
                        SpellActionExecutor.execute(action, world, effectiveCaster);
                    }

                    caster.setPos(originalX, originalY, originalZ);
                } else {
                    for (var action : timeline.actions()) {
                        SpellActionExecutor.execute(action, world, effectiveCaster);
                    }
                }

                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        };

        if (delayTicks > 0) {
            scheduler.schedule(() -> world.getServer().execute(executeTask), delayTicks * 50L, TimeUnit.MILLISECONDS);
        } else {
            world.getServer().execute(executeTask);
        }

        return future;
    }

    public static CompletableFuture<Void> scheduleDelayed(Runnable task, int delayTicks) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        scheduler.schedule(() -> {
            try {
                task.run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, delayTicks * 50L, TimeUnit.MILLISECONDS);
        return future;
    }

    private static boolean checkCondition(LootItemCondition condition, ServerLevel world, Entity caster) {
        LootParams lootWorldContext = new LootParams.Builder(world)
                .withParameter(LootContextParams.THIS_ENTITY, caster)
                .withParameter(LootContextParams.ORIGIN, caster.position())
                .create(LootContextParamSets.COMMAND);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).create(Optional.empty());
        return condition.test(lootContext);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}