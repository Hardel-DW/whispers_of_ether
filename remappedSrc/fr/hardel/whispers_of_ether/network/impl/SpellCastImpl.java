package fr.hardel.whispers_of_ether.network.impl;

import fr.hardel.whispers_of_ether.component.ModComponents;
import fr.hardel.whispers_of_ether.component.PlayerSpellComponent;
import fr.hardel.whispers_of_ether.network.WhispersOfEtherPacket.SpellCast;
import fr.hardel.whispers_of_ether.spell.Spell;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SpellCastImpl {

    public static void handle(SpellCast packet, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerPlayerEntity player = context.player();
            PlayerSpellComponent component = ModComponents.PLAYER_SPELL.get(player);

            if (!component.hasSpell(packet.spellId())) {
                return;
            }

            Spell spell = SpellResourceReloadListener.getSpell(packet.spellId());
            if (spell == null) {
                return;
            }

            spell.cast(player, packet.spellId());
        });
    }
}