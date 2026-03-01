package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.network.impl.MultiJumpImpl;
import fr.hardel.whispers_of_ether.network.impl.SpellCastImpl;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkHandler {
    public static void registerPayloadTypes() {
        PayloadTypeRegistry.playS2C().register(WhispersOfEtherPacket.RunicTableHistoryAdd.ID,
            WhispersOfEtherPacket.RunicTableHistoryAdd.CODEC);
        PayloadTypeRegistry.playS2C().register(WhispersOfEtherPacket.RunicTableHistoryClear.ID,
            WhispersOfEtherPacket.RunicTableHistoryClear.CODEC);
    }

    public static void registerClientPackets() {
    }

    public static void registerServerPackets() {
        PayloadTypeRegistry.playC2S().register(WhispersOfEtherPacket.SpellCast.ID,
            WhispersOfEtherPacket.SpellCast.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.SpellCast.ID, SpellCastImpl::handle);

        PayloadTypeRegistry.playC2S().register(WhispersOfEtherPacket.MultiJump.ID,
            WhispersOfEtherPacket.MultiJump.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.MultiJump.ID, MultiJumpImpl::handle);
    }
}