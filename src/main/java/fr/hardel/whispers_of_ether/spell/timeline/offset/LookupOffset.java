package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.world.entity.Entity;

import java.util.List;

public record LookupOffset(List<Position> positions) implements LoopOffset {

    public static final MapCodec<LookupOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Position.CODEC.listOf().fieldOf("positions").forGetter(LookupOffset::positions))
            .apply(instance, LookupOffset::new));

    @Override
    public LoopOffsetType<?> getType() {
        return LoopOffsetType.LOOKUP;
    }

    @Override
    public Position calculateOffset(int iteration, Entity caster) {
        if (positions.isEmpty()) {
            return new Position(0, 0, 0);
        }

        int index = iteration % positions.size();
        return positions.get(index);
    }
}