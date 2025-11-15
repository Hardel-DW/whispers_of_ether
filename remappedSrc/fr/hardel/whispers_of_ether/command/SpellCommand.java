package fr.hardel.whispers_of_ether.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SpellCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("spell")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("add")
                .then(CommandManager.argument("spell", IdentifierArgumentType.identifier())
                    .suggests((context, builder) -> CommandSource.suggestIdentifiers(SpellResourceReloadListener.getSpells().keySet(), builder))
                    .executes(SpellCommand::addSpell)
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(SpellCommand::addSpell))))
            .then(CommandManager.literal("remove")
                .then(CommandManager.argument("spell", IdentifierArgumentType.identifier())
                    .suggests((context, builder) -> CommandSource.suggestIdentifiers(SpellResourceReloadListener.getSpells().keySet(), builder))
                    .executes(SpellCommand::removeSpell)
                    .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(SpellCommand::removeSpell))))
            .then(CommandManager.literal("list")
                .executes(SpellCommand::listSpells)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .executes(SpellCommand::listSpells)))
            .then(CommandManager.literal("clear")
                .executes(SpellCommand::clearSpells)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .executes(SpellCommand::clearSpells))));
    }

    private static ServerPlayerEntity getTargetPlayer(CommandContext<ServerCommandSource> context) throws Exception {
        try {
            return EntityArgumentType.getPlayer(context, "player");
        } catch (IllegalArgumentException e) {
            return context.getSource().getPlayerOrThrow();
        }
    }

    private static int addSpell(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = getTargetPlayer(context);
            Identifier spellId = IdentifierArgumentType.getIdentifier(context, "spell");
            
            if (SpellResourceReloadListener.getSpell(spellId) == null) {
                context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.unknown", spellId));
                return 0;
            }
            var component = ModComponents.PLAYER_SPELL.get(player);
            
            if (component.hasSpell(spellId)) {
                context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.add.already_has",
                    player.getName().getString(), spellId.toString()));
                return 0;
            }
            
            component.addSpell(spellId);
            context.getSource().sendFeedback(() ->
                Text.translatable("command.whispers_of_ether.spell.add.success", spellId.toString(), player.getName().getString()), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int removeSpell(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = getTargetPlayer(context);
            Identifier spellId = IdentifierArgumentType.getIdentifier(context, "spell");
            
            if (SpellResourceReloadListener.getSpell(spellId) == null) {
                context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.unknown", spellId));
                return 0;
            }
            var component = ModComponents.PLAYER_SPELL.get(player);
            
            if (!component.hasSpell(spellId)) {
                context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.remove.not_found",
                    player.getName().getString(), spellId.toString()));
                return 0;
            }
            
            component.removeSpell(spellId);
            context.getSource().sendFeedback(() ->
                Text.translatable("command.whispers_of_ether.spell.remove.success", spellId.toString(), player.getName().getString()), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int listSpells(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = getTargetPlayer(context);
            var spells = ModComponents.PLAYER_SPELL.get(player).getSpellIds();
            
            if (spells.isEmpty()) {
                context.getSource().sendFeedback(() ->
                    Text.translatable("command.whispers_of_ether.spell.list.empty", player.getName().getString()), false);
                return 0;
            }
            
            context.getSource().sendFeedback(() ->
                Text.translatable("command.whispers_of_ether.spell.list.success",
                    player.getName().getString(), spells.size(), 
                    String.join(", ", spells.stream().map(Identifier::toString).toList())), false);
            return spells.size();
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int clearSpells(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = getTargetPlayer(context);
            var component = ModComponents.PLAYER_SPELL.get(player);
            int count = component.getSpellIds().size();
            
            if (count == 0) {
                context.getSource().sendFeedback(() ->
                    Text.translatable("command.whispers_of_ether.spell.clear.empty", player.getName().getString()), false);
                return 0;
            }
            
            component.clearSpells();
            context.getSource().sendFeedback(() ->
                Text.translatable("command.whispers_of_ether.spell.clear.success", count, player.getName().getString()), true);
            return count;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }
}