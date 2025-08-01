package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Position(float x, float y, float z) {
    public static final Position ZERO = new Position(0, 0, 0);
    
    public static final Codec<Position> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("x").forGetter(Position::x),
            Codec.FLOAT.fieldOf("y").forGetter(Position::y),
            Codec.FLOAT.fieldOf("z").forGetter(Position::z))
            .apply(instance, Position::new));
}