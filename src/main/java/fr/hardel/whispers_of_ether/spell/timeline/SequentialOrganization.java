package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public record SequentialOrganization(List<String> timelineIds) implements OrganizationTimeline {

    public static final MapCodec<SequentialOrganization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("timeline_ids").forGetter(SequentialOrganization::timelineIds))
            .apply(instance, SequentialOrganization::new));

    @Override
    public OrganizationType<?> getType() {
        return OrganizationType.SEQUENTIAL;
    }

    @Override
    public void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster) {
        executeSequentially(timelines, world, caster, 0, 0);
    }

    private void executeSequentially(List<TimelineAction> timelines, ServerWorld world, Entity caster, int index, int accumulatedDelay) {
        if (index >= timelineIds.size()) return;
        
        String timelineId = timelineIds.get(index);
        TimelineAction timeline = findTimelineById(timelines, timelineId);
        
        if (timeline != null) {
            TimelineScheduler.executeTimelineAsync(timeline, world, caster, accumulatedDelay)
                .thenRun(() -> {
                    int nextDelay = timeline.waitTime().orElse(0);
                    int nextDuration = timeline.duration();
                    executeSequentially(timelines, world, caster, index + 1, nextDelay + nextDuration);
                });
        } else {
            executeSequentially(timelines, world, caster, index + 1, accumulatedDelay);
        }
    }

    private TimelineAction findTimelineById(List<TimelineAction> timelines, String id) {
        return timelines.stream()
                .filter(timeline -> timeline.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}