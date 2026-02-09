package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.client.gui.screen.RunicTableScreen;
import fr.hardel.whispers_of_ether.client.gui.screen.RunicForgeScreen;
import fr.hardel.whispers_of_ether.client.gui.screen.RunicInfuserScreen;
import fr.hardel.whispers_of_ether.client.network.ClientNetworkHandler;
import fr.hardel.whispers_of_ether.client.particles.ModParticleClient;
import fr.hardel.whispers_of_ether.client.render.RenderSystem;
import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import fr.hardel.whispers_of_ether.menu.ModMenuTypes;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import fr.hardel.whispers_of_ether.client.render.entity.DamageIndicatorRenderer;
import fr.hardel.whispers_of_ether.client.render.entity.TargetDummyRenderer;
import fr.hardel.whispers_of_ether.entity.ModEntities;
import fr.hardel.whispers_of_ether.client.render.entity.model.TargetDummyModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhispersOfEtherClient implements ClientModInitializer {
    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ModKeyBindings.register();
        ModParticleClient.register();
        SpellCastHandler.initialize();
        NetworkHandler.registerClientPackets();
        ClientNetworkHandler.register();
        RenderSystem.register();

        MenuScreens.register(ModMenuTypes.RUNIC_TABLE, RunicTableScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_FORGE, RunicForgeScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_INFUSER, RunicInfuserScreen::new);
        EntityRenderers.register(ModEntities.TARGET_DUMMY, TargetDummyRenderer::new);
        EntityRenderers.register(ModEntities.DAMAGE_INDICATOR, DamageIndicatorRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(TargetDummyModel.LAYER_LOCATION, TargetDummyModel::createBodyLayer);

        WorldRenderEvents.AFTER_ENTITIES.register(RenderSystem.getInstance()::renderAll);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(MOD_ID, "spell_selector"),
            SpellSelector::render);
    }

    public static RenderSystem getRenderSystem() {
        return RenderSystem.getInstance();
    }
}
