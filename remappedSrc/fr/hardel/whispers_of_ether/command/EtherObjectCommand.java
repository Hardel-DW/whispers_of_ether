package fr.hardel.whispers_of_ether.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponents;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponent;
import java.util.concurrent.CompletableFuture;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EtherObjectCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("etherobj")
                .then(CommandManager.literal("create")
                        .then(CommandManager.argument("id", StringArgumentType.string())
                                .then(CommandManager.argument("type", StringArgumentType.string())
                                        .suggests(EtherObjectCommand::suggestTypes)
                                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                                .then(CommandManager
                                                        .argument("radius", FloatArgumentType.floatArg(0.1f, 256.0f))
                                                        .then(CommandManager
                                                                .argument("strength",
                                                                        FloatArgumentType.floatArg(0.0f, 1000.0f))
                                                                .executes(EtherObjectCommand::create)))))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("id", StringArgumentType.string())
                                .suggests(EtherObjectCommand::suggestExistingIds)
                                .executes(EtherObjectCommand::remove)))
                .then(CommandManager.literal("clear").executes(EtherObjectCommand::clear)));
    }

    private static CompletableFuture<Suggestions> suggestTypes(CommandContext<ServerCommandSource> ctx,
            SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(
                SceneObjectType.REGISTRY.getIds().stream().map(Identifier::getPath), builder);
    }

    private static CompletableFuture<Suggestions> suggestExistingIds(CommandContext<ServerCommandSource> ctx,
            SuggestionsBuilder builder) {
        var world = ctx.getSource().getWorld();
        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        return CommandSource.suggestMatching(comp.getAll().stream().map(SceneObject::id), builder);
    }

    private static int create(CommandContext<ServerCommandSource> ctx) {
        var src = ctx.getSource();
        ServerWorld world = src.getWorld();

        String id = StringArgumentType.getString(ctx, "id");
        String typeStr = StringArgumentType.getString(ctx, "type");
        Identifier typeId = typeStr.contains(":") ? Identifier.tryParse(typeStr)
                : Identifier.of(fr.hardel.whispers_of_ether.WhispersOfEther.MOD_ID, typeStr);
        SceneObjectType type = typeId == null ? null : SceneObjectType.REGISTRY.get(typeId);
        if (type == null) {
            src.sendError(Text.translatable("command.whispers_of_ether.etherobj.unknown_type", typeStr));
            return 0;
        }

        BlockPos bp = BlockPosArgumentType.getBlockPos(ctx, "pos");
        float radius = FloatArgumentType.getFloat(ctx, "radius");
        float strength = FloatArgumentType.getFloat(ctx, "strength");

        Vec3d pos = new Vec3d(bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5);
        var obj = new SceneObject(id, type, pos, radius, strength);

        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        boolean isNew = comp.upsert(obj);
        Text displayName = SceneObjectTypes.getDisplayName(type);
        String actionKey = isNew ? "command.whispers_of_ether.etherobj.created" : "command.whispers_of_ether.etherobj.updated";
        src.sendFeedback(() -> Text.translatable(actionKey, id, displayName, bp.getX(), bp.getY(), bp.getZ(), radius, strength), false);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> ctx) {
        var src = ctx.getSource();
        ServerWorld world = src.getWorld();
        String id = StringArgumentType.getString(ctx, "id");
        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        if (comp.remove(id)) {
            src.sendFeedback(() -> Text.translatable("command.whispers_of_ether.etherobj.removed", id), false);
            return 1;
        }
        src.sendError(Text.translatable("command.whispers_of_ether.etherobj.not_found", id));
        return 0;
    }

    private static int clear(CommandContext<ServerCommandSource> ctx) {
        var src = ctx.getSource();
        ServerWorld world = src.getWorld();
        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        int count = comp.getAll().size();
        comp.clear();
        src.sendFeedback(() -> Text.translatable("command.whispers_of_ether.etherobj.cleared", count), false);
        return 1;
    }
}
