package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.network.impl.SpellCastImpl;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkHandler {

    public static void registerClientPackets() {
        // Client registration is handled in registerServerPackets
    }

    public static void registerServerPackets() {
        PayloadTypeRegistry.playC2S().register(WhispersOfEtherPacket.SpellCast.ID,
                WhispersOfEtherPacket.SpellCast.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.SpellCast.ID, SpellCastImpl::handle);
    }
}