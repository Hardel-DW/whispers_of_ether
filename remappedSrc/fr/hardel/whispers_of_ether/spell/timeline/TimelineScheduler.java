package fr.hardel.whispers_of_ether.spell.timeline;

import fr.hardel.whispers_of_ether.spell.SpellActionExecutor;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;

public class TimelineScheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerWorld world,
                                                               Entity caster, int delayTicks) {
        return executeTimelineAsync(timeline, world, caster, delayTicks, null);
    }

    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerWorld world,
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

                    caster.setPosition(
                            originalX + offset.x(),
                            originalY + offset.y(),
                            originalZ + offset.z());

                    for (var action : timeline.actions()) {
                        SpellActionExecutor.execute(action, world, effectiveCaster);
                    }

                    caster.setPosition(originalX, originalY, originalZ);
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

    private static boolean checkCondition(LootCondition condition, ServerWorld world, Entity caster) {
        LootWorldContext lootWorldContext = new LootWorldContext.Builder(world)
                .add(LootContextParameters.THIS_ENTITY, caster)
                .add(LootContextParameters.ORIGIN, caster.getEntityPos())
                .build(LootContextTypes.COMMAND);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());
        return condition.test(lootContext);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}