package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.Codec;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

public sealed interface OrganizationTimeline
        permits SequentialOrganization, ParallelOrganization,
        RandomOrganization, LoopOrganization, GroupOrganization {

    Codec<OrganizationTimeline> CODEC = OrganizationType.REGISTRY.getCodec()
        .dispatch("type", OrganizationTimeline::getType, OrganizationType::codec);

    OrganizationType<?> getType();
    
    void execute(List<TimelineAction> timelines, ServerWorld world, Entity caster);
}