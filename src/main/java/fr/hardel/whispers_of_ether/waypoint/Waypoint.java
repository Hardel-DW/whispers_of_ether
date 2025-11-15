package fr.hardel.whispers_of_ether.waypoint;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Waypoint {
    public static final Codec<Waypoint> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf("name").forGetter(waypoint -> waypoint.name),
            BlockPos.CODEC.fieldOf("position").forGetter(waypoint -> waypoint.position),
            Codec.INT.fieldOf("color").forGetter(waypoint -> waypoint.color)
        ).apply(instance, Waypoint::new)
    );

    private final String name;
    private final BlockPos position;
    private final int color;

    public Waypoint(String name, BlockPos position, int color) {
        this.name = name;
        this.position = position;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public BlockPos getPosition() {
        return position;
    }

    public Vec3 getCenterPosition() {
        return Vec3.atCenterOf(position);
    }

    public int getColor() {
        return color;
    }

    public double getDistanceTo(Vec3 playerPos) {
        return getCenterPosition().distanceTo(playerPos);
    }
}