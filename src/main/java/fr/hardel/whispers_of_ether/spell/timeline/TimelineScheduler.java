package fr.hardel.whispers_of_ether.spell.timeline;

import fr.hardel.whispers_of_ether.spell.SpellActionExecutor;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.context.LootWorldContext;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimelineScheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerWorld world,
            Entity caster, int delayTicks) {
        return executeTimelineAsync(timeline, world, caster, delayTicks, null);
    }
    
    public static CompletableFuture<Void> executeTimelineAsync(TimelineAction timeline, ServerWorld world,
            Entity caster, int delayTicks, Position offset) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (delayTicks > 0) {
                    Thread.sleep(delayTicks * 50L); // 50ms par tick
                }

                if (timeline.condition().isPresent() && !checkCondition(timeline.condition().get(), world, caster)) {
                    return;
                }

                // Créer un caster temporaire avec offset si nécessaire
                Entity effectiveCaster = caster;
                if (offset != null) {
                    // TODO: Créer un caster proxy avec position décalée
                    // Pour l'instant, on utilise le caster original
                    effectiveCaster = caster;
                }

                for (var action : timeline.actions()) {
                    SpellActionExecutor.execute(action, world, effectiveCaster);
                }

                if (timeline.duration() > 0) {
                    Thread.sleep(timeline.duration() * 50L);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, scheduler);
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
                .add(LootContextParameters.ORIGIN, caster.getPos())
                .build(LootContextTypes.COMMAND);
        LootContext lootContext = new LootContext.Builder(lootWorldContext).build(Optional.empty());
        return condition.test(lootContext);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}