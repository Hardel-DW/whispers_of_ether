package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.client.render.PipelineFactory;
import fr.hardel.whispers_of_ether.client.render.RendererFactory;
import fr.hardel.whispers_of_ether.client.render.RenderSystem;
import fr.hardel.whispers_of_ether.client.screen.WaypointRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
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

    private static RenderSystem renderSystem;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Whispers of Ether client");
        ModKeyBindings.register();
        SpellCastHandler.initialize();
        NetworkHandler.registerClientPackets();
        WaypointRenderer.register();
        
        PipelineFactory pipelineFactory = new PipelineFactory();
        RendererFactory rendererFactory = new RendererFactory(pipelineFactory);
        renderSystem = rendererFactory.createRenderSystem();
        
        WorldRenderEvents.AFTER_TRANSLUCENT.register(renderSystem::renderAll);
        
        HudElementRegistry.addLast(Identifier.of(MOD_ID, "spell_selector"), SpellSelector::render);
    }

    public static RenderSystem getRenderSystem() {
        return renderSystem;
    }
}
