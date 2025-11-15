package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.timeline.offset.LoopOffset;
import java.util.List;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public record LoopOrganization(String timelineId, int iterations, Optional<Integer> delay, Optional<LoopOffset> offset)
        implements OrganizationTimeline {

    public static final MapCodec<LoopOrganization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("timeline_id").forGetter(LoopOrganization::timelineId),
            Codec.INT.fieldOf("iterations").forGetter(LoopOrganization::iterations),
            Codec.INT.optionalFieldOf("delay").forGetter(LoopOrganization::delay),
            LoopOffset.CODEC.optionalFieldOf("offset").forGetter(LoopOrganization::offset))
            .apply(instance, LoopOrganization::new));

    @Override
    public OrganizationType<?> getType() {
        return OrganizationType.LOOP;
    }

    @Override
    public void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster) {
        TimelineAction timeline = findTimelineById(timelines, timelineId);
        if (timeline == null)
            return;

        executeLoop(timeline, world, caster, 0);
    }

    private void executeLoop(TimelineAction timeline, ServerWorld world, Entity caster, int currentIteration) {
        if (currentIteration >= iterations)
            return;

        int waitTime = timeline.waitTime().orElse(0);
        if (currentIteration > 0 && delay.isPresent()) {
            waitTime += delay.get();
        }

        fr.hardel.whispers_of_ether.spell.target.position.Position offsetPos = null;
        if (offset.isPresent()) {
            offsetPos = offset.get().calculateOffset(currentIteration, caster);
        }

        TimelineScheduler.executeTimelineAsync(timeline, world, caster, waitTime, offsetPos)
                .thenRun(() -> executeLoop(timeline, world, caster, currentIteration + 1));
    }

    private TimelineAction findTimelineById(List<TimelineAction> timelines, String id) {
        return timelines.stream()
                .filter(timeline -> timeline.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}