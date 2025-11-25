package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.network.impl.MultiJumpImpl;
import fr.hardel.whispers_of_ether.network.impl.SpellCastImpl;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkHandler {
    public static void registerClientPackets() {
        PayloadTypeRegistry.playS2C().register(WhispersOfEtherPacket.ForgeHistoryAdd.ID,
            WhispersOfEtherPacket.ForgeHistoryAdd.CODEC);
        PayloadTypeRegistry.playS2C().register(WhispersOfEtherPacket.ForgeHistoryClear.ID,
            WhispersOfEtherPacket.ForgeHistoryClear.CODEC);
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