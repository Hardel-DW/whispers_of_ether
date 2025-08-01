package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.Codec;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.entity.Entity;

public sealed interface LoopOffset 
    permits RandomBoxOffset, LookupOffset, RandomValueOffset, ForwardOffset {

    Codec<LoopOffset> CODEC = LoopOffsetType.REGISTRY.getCodec()
        .dispatch("type", LoopOffset::getType, LoopOffsetType::codec);

    LoopOffsetType<?> getType();
    
    Position calculateOffset(int iteration, Entity caster);
}