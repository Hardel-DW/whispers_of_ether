package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public sealed interface OrganizationTimeline
        permits SequentialOrganization, ParallelOrganization,
        RandomOrganization, LoopOrganization, GroupOrganization {

    Codec<OrganizationTimeline> CODEC = OrganizationType.REGISTRY.byNameCodec()
        .dispatch("type", OrganizationTimeline::getType, OrganizationType::codec);

    OrganizationType<?> getType();
    
    void execute(List<TimelineAction> timelines, ServerLevel world, Entity caster);
}