package fr.hardel.whispers_of_ether.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.hardel.whispers_of_ether.component.ModComponents;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class WaypointCommand {

    private static final SimpleCommandExceptionType INVALID_COLOR_EXCEPTION = new SimpleCommandExceptionType(
            Text.translatable("command.whispers_of_ether.waypoint.invalid_color"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("waypoint")
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .then(CommandManager.argument("color", StringArgumentType.string())
                                                .executes(WaypointCommand::addWaypoint)))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .executes(WaypointCommand::removeWaypoint)))
                .then(CommandManager.literal("list")
                        .executes(WaypointCommand::listWaypoints))
                .then(CommandManager.literal("clear")
                        .executes(WaypointCommand::clearWaypoints)));
    }

    private static int addWaypoint(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        String name = StringArgumentType.getString(context, "name");
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
        String colorString = StringArgumentType.getString(context, "color");

        int color = parseColor(colorString);

        var waypointComponent = ModComponents.WAYPOINTS.get(source.getPlayer());
        waypointComponent.addWaypoint(name, pos, color);

        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.add.success",
                name, pos.getX(), pos.getY(), pos.getZ(), String.format("%06X", color)), false);
        return 1;
    }

    private static int removeWaypoint(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        String name = StringArgumentType.getString(context, "name");
        var waypointComponent = ModComponents.WAYPOINTS.get(source.getPlayer());

        if (waypointComponent.removeWaypoint(name)) {
            source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.remove.success", name), false);
            return 1;
        } else {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.remove.not_found", name));
            return 0;
        }
    }

    private static int listWaypoints(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        var waypointComponent = ModComponents.WAYPOINTS.get(source.getPlayer());
        var waypoints = waypointComponent.getWaypoints();

        if (waypoints.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.list.empty"), false);
            return 0;
        }

        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.list.header", waypoints.size()), false);
        for (var waypoint : waypoints) {
            var pos = waypoint.getPosition();
            source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.list.item",
                    waypoint.getName(), pos.getX(), pos.getY(), pos.getZ()), false);
        }
        return waypoints.size();
    }

    private static int clearWaypoints(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        var waypointComponent = ModComponents.WAYPOINTS.get(source.getPlayer());
        waypointComponent.clearWaypoints();

        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.waypoint.clear.success"), false);
        return 1;
    }

    private static int parseColor(String colorString) throws CommandSyntaxException {
        try {
            if (colorString.matches("[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6}")) {
                var hex = colorString;
                if (hex.length() == 3) {
                    hex = hex.chars()
                            .mapToObj(c -> String.valueOf((char) c).repeat(2))
                            .collect(java.util.stream.Collectors.joining());
                }
                return Integer.parseInt(hex, 16);
            }

            int color = Integer.parseInt(colorString);
            if (color < 0 || color > 0xFFFFFF) {
                throw INVALID_COLOR_EXCEPTION.create();
            }
            return color;
        } catch (NumberFormatException e) {
            throw INVALID_COLOR_EXCEPTION.create();
        }
    }
}