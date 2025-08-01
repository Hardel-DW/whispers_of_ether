package fr.hardel.whispers_of_ether.spell.target.position;

public record Position(float x, float y, float z) {
    public static final Position ZERO = new Position(0, 0, 0);
}