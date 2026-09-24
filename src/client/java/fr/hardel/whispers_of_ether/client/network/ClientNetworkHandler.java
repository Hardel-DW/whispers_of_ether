package fr.hardel.whispers_of_ether.client.network;

import fr.hardel.whispers_of_ether.client.gui.screens.RunicTableScreen;
import fr.hardel.whispers_of_ether.network.WhispersOfEtherPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

public class ClientNetworkHandler {
	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.RunicTableHistoryAdd.ID, (payload, context) -> context.client().execute(() -> {
            if (Minecraft.getInstance().gui.screen() instanceof RunicTableScreen screen) {
                screen.addHistoryEntry(payload.entry());
            }
        }));

		ClientPlayNetworking.registerGlobalReceiver(WhispersOfEtherPacket.RunicTableHistoryClear.ID, (payload, context) -> context.client().execute(() -> {
            if (Minecraft.getInstance().gui.screen() instanceof RunicTableScreen screen) {
                screen.clearHistory();
            }
        }));
	}
}
