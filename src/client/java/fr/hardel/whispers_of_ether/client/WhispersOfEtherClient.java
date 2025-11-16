package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.client.particles.ModParticleClient;
import fr.hardel.whispers_of_ether.client.render.RenderSystem;
import fr.hardel.whispers_of_ether.client.screen.WaypointRenderer;
import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhispersOfEtherClient implements ClientModInitializer {
    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Whispers of Ether client");
        ModKeyBindings.register();
        ModParticleClient.register();
        SpellCastHandler.initialize();
        NetworkHandler.registerClientPackets();
        WaypointRenderer.register();
        RenderSystem.register();

        WorldRenderEvents.AFTER_ENTITIES.register(RenderSystem.getInstance()::renderAll);
        HudElementRegistry.addLast(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spell_selector"),
                SpellSelector::render);
    }

    public static RenderSystem getRenderSystem() {
        return RenderSystem.getInstance();
    }
}
