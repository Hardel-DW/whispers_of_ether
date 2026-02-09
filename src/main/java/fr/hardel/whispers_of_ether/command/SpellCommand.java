package fr.hardel.whispers_of_ether.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SpellCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spell")
            .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
            .then(Commands.literal("add")
                .then(Commands.argument("spell", IdentifierArgument.id())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(SpellResourceReloadListener.getSpells().keySet(), builder))
                    .executes(SpellCommand::addSpell)
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(SpellCommand::addSpell))))
            .then(Commands.literal("remove")
                .then(Commands.argument("spell", IdentifierArgument.id())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(SpellResourceReloadListener.getSpells().keySet(), builder))
                    .executes(SpellCommand::removeSpell)
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(SpellCommand::removeSpell))))
            .then(Commands.literal("list")
                .executes(SpellCommand::listSpells)
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(SpellCommand::listSpells)))
            .then(Commands.literal("clear")
                .executes(SpellCommand::clearSpells)
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(SpellCommand::clearSpells))));
    }

    private static ServerPlayer getTargetPlayer(CommandContext<CommandSourceStack> context) throws Exception {
        try {
            return EntityArgument.getPlayer(context, "player");
        } catch (IllegalArgumentException e) {
            return context.getSource().getPlayerOrException();
        }
    }

    private static int addSpell(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = getTargetPlayer(context);
            Identifier spellId = IdentifierArgument.getId(context, "spell");

            if (SpellResourceReloadListener.getSpell(spellId) == null) {
                context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.unknown", spellId));
                return 0;
            }
            var component = ModComponents.PLAYER_SPELL.get(player);

            if (component.hasSpell(spellId)) {
                context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.add.already_has",
                    player.getName().getString(), spellId.toString()));
                return 0;
            }

            component.addSpell(spellId);
            context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.add.success", spellId.toString(), player.getName().getString()), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int removeSpell(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = getTargetPlayer(context);
            Identifier spellId = IdentifierArgument.getId(context, "spell");

            if (SpellResourceReloadListener.getSpell(spellId) == null) {
                context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.unknown", spellId));
                return 0;
            }
            var component = ModComponents.PLAYER_SPELL.get(player);

            if (!component.hasSpell(spellId)) {
                context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.remove.not_found",
                    player.getName().getString(), spellId.toString()));
                return 0;
            }

            component.removeSpell(spellId);
            context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.remove.success", spellId.toString(), player.getName().getString()), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int listSpells(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = getTargetPlayer(context);
            var spells = ModComponents.PLAYER_SPELL.get(player).getSpellIds();

            if (spells.isEmpty()) {
                context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.list.empty", player.getName().getString()), false);
                return 0;
            }

            context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.list.success",
                player.getName().getString(), spells.size(),
                String.join(", ", spells.stream().map(Identifier::toString).toList())), false);
            return spells.size();
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }

    private static int clearSpells(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = getTargetPlayer(context);
            var component = ModComponents.PLAYER_SPELL.get(player);
            int count = component.getSpellIds().size();

            if (count == 0) {
                context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.clear.empty", player.getName().getString()), false);
                return 0;
            }

            component.clearSpells();
            context.getSource().sendSuccess(() -> Component.translatable("command.whispers_of_ether.spell.clear.success", count, player.getName().getString()), true);
            return count;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("command.whispers_of_ether.spell.error", e.getMessage()));
            return 0;
        }
    }
}