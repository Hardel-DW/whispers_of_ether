package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.waypoint.Waypoint;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

public class WaypointComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final List<Waypoint> waypoints = new ArrayList<>();

    public WaypointComponent(PlayerEntity player) {
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
    public void readData(ReadView readView) {
        waypoints.clear();
        readView.read("waypoints", Waypoint.CODEC.listOf()).ifPresent(waypoints::addAll);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("waypoints", Waypoint.CODEC.listOf(), waypoints);
    }
}