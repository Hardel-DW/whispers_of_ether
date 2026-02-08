package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.waypoint.Waypoint;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

public class WaypointComponent implements AutoSyncedComponent {
    private final Player player;
    private final List<Waypoint> waypoints = new ArrayList<>();

    public WaypointComponent(Player player) {
        this.player = player;
    }

    public List<Waypoint> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    public void addWaypoint(String name, BlockPos position, int color) {
        waypoints.add(new Waypoint(name, position, color));
        ModComponents.WAYPOINTS.sync(player);
    }

    public boolean removeWaypoint(String name) {
        boolean removed = waypoints.removeIf(waypoint -> waypoint.getName().equals(name));
        if (removed) {
            ModComponents.WAYPOINTS.sync(player);
        }
        return removed;
    }

    public void clearWaypoints() {
        if (!waypoints.isEmpty()) {
            waypoints.clear();
            ModComponents.WAYPOINTS.sync(player);
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
        waypoints.clear();
        if (tag.contains("waypoints")) {
            Waypoint.CODEC.listOf()
                .parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("waypoints"))
                .result()
                .ifPresent(waypoints::addAll);
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
        Waypoint.CODEC.listOf()
            .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), waypoints)
            .result()
            .ifPresent(encoded -> tag.put("waypoints", encoded));
    }
}