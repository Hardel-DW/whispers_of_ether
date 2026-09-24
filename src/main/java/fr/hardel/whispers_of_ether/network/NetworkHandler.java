package fr.hardel.whispers_of_ether.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkHandler {
    public static void registerClientPackets() {
        PayloadTypeRegistry.clientboundPlay().register(WhispersOfEtherPacket.RunicTableHistoryAdd.ID,
            WhispersOfEtherPacket.RunicTableHistoryAdd.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(WhispersOfEtherPacket.RunicTableHistoryClear.ID,
            WhispersOfEtherPacket.RunicTableHistoryClear.CODEC);
    }

    public static void registerServerPackets() {
        PayloadTypeRegistry.serverboundPlay().register(WhispersOfEtherPacket.MultiJump.ID,
            WhispersOfEtherPacket.MultiJump.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.MultiJump.ID, MultiJumpImpl::handle);
    }
}