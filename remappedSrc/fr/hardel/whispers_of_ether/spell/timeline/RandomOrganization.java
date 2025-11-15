package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

public record RandomOrganization(List<RandomChoice> choices) implements OrganizationTimeline {
    
    public static final MapCodec<RandomOrganization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RandomChoice.CODEC.listOf().fieldOf("choices").forGetter(RandomOrganization::choices)
    ).apply(instance, RandomOrganization::new));

    @Override
    public OrganizationType<?> getType() {
        return OrganizationType.RANDOM;
    }

    @Override
    public void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster) {
        if (choices.isEmpty()) return;
        
        int totalWeight = choices.stream().mapToInt(RandomChoice::weight).sum();
        if (totalWeight <= 0) return;
        
        Random random = world.getRandom();
        int randomValue = random.nextInt(totalWeight);
        
        int currentWeight = 0;
        for (RandomChoice choice : choices) {
            currentWeight += choice.weight();
            if (randomValue < currentWeight) {
                TimelineAction timeline = findTimelineById(timelines, choice.timelineId());
                if (timeline != null) {
                    int waitTime = timeline.waitTime().orElse(0);
                    TimelineScheduler.executeTimelineAsync(timeline, world, caster, waitTime);
                }
                break;
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