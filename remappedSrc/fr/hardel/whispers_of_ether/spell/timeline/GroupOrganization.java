package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public record GroupOrganization(OrganizationTimeline organization) implements OrganizationTimeline {
    
    public static final MapCodec<GroupOrganization> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        OrganizationTimeline.CODEC.fieldOf("organization").forGetter(GroupOrganization::organization)
    ).apply(instance, GroupOrganization::new));

    @Override
    public OrganizationType<?> getType() {
        return OrganizationType.GROUP;
    }

    @Override
    public void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster) {
        organization.execute(timelines, world, caster);
    }
}