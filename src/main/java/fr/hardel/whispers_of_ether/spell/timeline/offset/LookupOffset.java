package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.target.position.Position;
import net.minecraft.entity.Entity;

import java.util.List;

public record LookupOffset(List<Position> positions) implements LoopOffset {

    public static final MapCodec<LookupOffset> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RecordCodecBuilder.mapCodec(posInstance -> posInstance.group(
                    Codec.FLOAT.fieldOf("x").forGetter(pos -> ((Position) pos).x()),
                    Codec.FLOAT.fieldOf("y").forGetter(pos -> ((Position) pos).y()),
                    Codec.FLOAT.fieldOf("z").forGetter(pos -> ((Position) pos).z())).apply(posInstance, Position::new))
                    .codec().listOf().fieldOf("positions").forGetter(LookupOffset::positions))
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