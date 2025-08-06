package fr.hardel.whispers_of_ether.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class EtherSphereCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("ethersphere")
                .then(CommandManager.literal("create")
                        .then(CommandManager.argument("id", StringArgumentType.string())
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .then(CommandManager.argument("radius", FloatArgumentType.floatArg(0.1f, 50.0f))
                                                .executes(EtherSphereCommand::createSphere)))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("id", StringArgumentType.string())
                                .executes(EtherSphereCommand::removeSphere)))
                .then(CommandManager.literal("clear")
                        .executes(EtherSphereCommand::clearSpheres)));
    }

    private static int createSphere(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        String id = StringArgumentType.getString(context, "id");
        BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
        float radius = FloatArgumentType.getFloat(context, "radius");

        // Add sphere to client manager if we're on client side (integrated server)
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Vec3d position = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            try {
                Class<?> managerClass = Class.forName("fr.hardel.whispers_of_ether.client.render.EtherSphereManager");
                Object manager = managerClass.getMethod("getInstance").invoke(null);
                managerClass.getMethod("addSphere", String.class, Vec3d.class, float.class)
                    .invoke(manager, id, position, radius);
            } catch (Exception e) {
                // Ignore if client classes are not available
            }
        }

        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.ethersphere.create.success", 
                id, pos.getX(), pos.getY(), pos.getZ(), radius), false);
        return 1;
    }

    private static int removeSphere(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        String id = StringArgumentType.getString(context, "id");
        
        // Remove sphere from client manager if we're on client side (integrated server)
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class<?> managerClass = Class.forName("fr.hardel.whispers_of_ether.client.render.EtherSphereManager");
                Object manager = managerClass.getMethod("getInstance").invoke(null);
                managerClass.getMethod("removeSphere", String.class).invoke(manager, id);
            } catch (Exception e) {
                // Ignore if client classes are not available
            }
        }
        
        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.ethersphere.remove.success", id), false);
        return 1;
    }

    private static int clearSpheres(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        if (source.getPlayer() == null) {
            source.sendError(Text.translatable("command.whispers_of_ether.waypoint.player_only"));
            return 0;
        }

        // Clear spheres from client manager if we're on client side (integrated server)
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                Class<?> managerClass = Class.forName("fr.hardel.whispers_of_ether.client.render.EtherSphereManager");
                Object manager = managerClass.getMethod("getInstance").invoke(null);
                managerClass.getMethod("clearSpheres").invoke(manager);
            } catch (Exception e) {
                // Ignore if client classes are not available
            }
        }

        source.sendFeedback(() -> Text.translatable("command.whispers_of_ether.ethersphere.clear.success"), false);
        return 1;
    }
}