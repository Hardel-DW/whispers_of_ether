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
import net.minecraft.resources.ResourceLocation;
import fr.hardel.whispers_of_ether.object.SceneObject;
import fr.hardel.whispers_of_ether.object.SceneObjectsComponent;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import java.util.concurrent.CompletableFuture;

public class EtherObjectCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("etherobj")
                .then(Commands.literal("create")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .then(Commands.argument("type", StringArgumentType.string())
                                        .suggests(EtherObjectCommand::suggestTypes)
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .then(Commands
                                                        .argument("radius", FloatArgumentType.floatArg(0.1f, 256.0f))
                                                        .then(Commands
                                                                .argument("strength",
                                                                        FloatArgumentType.floatArg(0.0f, 1000.0f))
                                                                .executes(EtherObjectCommand::create)))))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("id", StringArgumentType.string())
                                .suggests(EtherObjectCommand::suggestExistingIds)
                                .executes(EtherObjectCommand::remove)))
                .then(Commands.literal("clear").executes(EtherObjectCommand::clear)));
    }

    private static CompletableFuture<Suggestions> suggestTypes(CommandContext<CommandSourceStack> ctx,
            SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
                SceneObjectType.REGISTRY.keySet().stream().map(ResourceLocation::getPath), builder);
    }

    private static CompletableFuture<Suggestions> suggestExistingIds(CommandContext<CommandSourceStack> ctx,
            SuggestionsBuilder builder) {
        var world = ctx.getSource().getLevel();
        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        return SharedSuggestionProvider.suggest(comp.getAll().stream().map(SceneObject::id), builder);
    }

    private static int create(CommandContext<CommandSourceStack> ctx) {
        var src = ctx.getSource();
        ServerLevel world = src.getLevel();

        String id = StringArgumentType.getString(ctx, "id");
        String typeStr = StringArgumentType.getString(ctx, "type");
        ResourceLocation typeId = typeStr.contains(":") ? ResourceLocation.tryParse(typeStr)
                : ResourceLocation.fromNamespaceAndPath(fr.hardel.whispers_of_ether.WhispersOfEther.MOD_ID, typeStr);
        SceneObjectType type = typeId == null ? null : SceneObjectType.REGISTRY.get(typeId);
        if (type == null) {
            src.sendFailure(Component.translatable("command.whispers_of_ether.etherobj.unknown_type", typeStr));
            return 0;
        }

        BlockPos bp = BlockPosArgument.getBlockPos(ctx, "pos");
        float radius = FloatArgumentType.getFloat(ctx, "radius");
        float strength = FloatArgumentType.getFloat(ctx, "strength");

        Vec3 pos = new Vec3(bp.getX() + 0.5, bp.getY() + 0.5, bp.getZ() + 0.5);
        var obj = new SceneObject(id, type, pos, radius, strength);

        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        boolean isNew = comp.upsert(obj);
        Component displayName = SceneObjectTypes.getDisplayName(type);
        String actionKey = isNew ? "command.whispers_of_ether.etherobj.created" : "command.whispers_of_ether.etherobj.updated";
        src.sendSuccess(() -> Component.translatable(actionKey, id, displayName, bp.getX(), bp.getY(), bp.getZ(), radius, strength), false);
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) {
        var src = ctx.getSource();
        ServerLevel world = src.getLevel();
        String id = StringArgumentType.getString(ctx, "id");
        SceneObjectsComponent comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        if (comp.remove(id)) {
            src.sendSuccess(() -> Component.translatable("command.whispers_of_ether.etherobj.removed", id), false);
            return 1;
        }
        src.sendFailure(Component.translatable("command.whispers_of_ether.etherobj.not_found", id));
        return 0;
    }

    private static int clear(CommandContext<CommandSourceStack> ctx) {
        var src = ctx.getSource();
        ServerLevel world = src.getLevel();
        var comp = SceneObjectsComponents.SCENE_OBJECTS.get(world);
        int count = comp.getAll().size();
        comp.clear();
        src.sendSuccess(() -> Component.translatable("command.whispers_of_ether.etherobj.cleared", count), false);
        return 1;
    }
}
