package fr.hardel.whispers_of_ether.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import fr.hardel.whispers_of_ether.network.NetworkHandler;

public class WhispersOfEtherClient implements ClientModInitializer {

    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Whispers of Ether client");
        ModKeyBindings.register();
        SpellCastHandler.initialize();
        NetworkHandler.registerClientPackets();
        HudElementRegistry.addLast(Identifier.of(MOD_ID, "spell_selector"), SpellSelector::render);
    }
}
