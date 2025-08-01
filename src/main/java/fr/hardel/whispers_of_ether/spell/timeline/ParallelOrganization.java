package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public record ParallelOrganization(List<String> timelineIds) implements OrganizationTimeline {

    public static final MapCodec<ParallelOrganization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.listOf().fieldOf("timeline_ids").forGetter(ParallelOrganization::timelineIds))
            .apply(instance, ParallelOrganization::new));

    @Override
    public OrganizationType<?> getType() {
        return OrganizationType.PARALLEL;
    }

    @Override
    public void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster) {
        for (String timelineId : timelineIds) {
            TimelineAction timeline = findTimelineById(timelines, timelineId);
            if (timeline != null) {
                int waitTime = timeline.waitTime().orElse(0);
                TimelineScheduler.executeTimelineAsync(timeline, world, caster, waitTime);
            }
        }
    }

    private TimelineAction findTimelineById(List<TimelineAction> timelines, String id) {
        return timelines.stream()
                .filter(timeline -> timeline.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}