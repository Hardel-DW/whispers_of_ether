package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.gui.screens.RunicTableScreen;
import fr.hardel.whispers_of_ether.client.gui.screens.RunicForgeScreen;
import fr.hardel.whispers_of_ether.client.gui.screens.RunicInfuserScreen;
import fr.hardel.whispers_of_ether.client.network.ClientNetworkHandler;
import fr.hardel.whispers_of_ether.client.particle.ModParticleClient;
import fr.hardel.whispers_of_ether.client.renderer.item.properties.numeric.RuneTier;
import fr.hardel.whispers_of_ether.world.inventory.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import fr.hardel.whispers_of_ether.client.renderer.entity.DamageIndicatorRenderer;
import fr.hardel.whispers_of_ether.client.renderer.entity.TargetDummyRenderer;
import fr.hardel.whispers_of_ether.world.entity.ModEntities;
import fr.hardel.whispers_of_ether.client.renderer.entity.model.TargetDummyModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.resources.Identifier;

public class WhispersOfEtherClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModParticleClient.register();
        ClientNetworkHandler.register();

        MenuScreens.register(ModMenuTypes.RUNIC_TABLE, RunicTableScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_FORGE, RunicForgeScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_INFUSER, RunicInfuserScreen::new);
        EntityRenderers.register(ModEntities.TARGET_DUMMY, TargetDummyRenderer::new);
        EntityRenderers.register(ModEntities.DAMAGE_INDICATOR, DamageIndicatorRenderer::new);
        ModelLayerRegistry.registerModelLayer(TargetDummyModel.LAYER_LOCATION, TargetDummyModel::createBodyLayer);
        RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "rune_tier"), RuneTier.MAP_CODEC);
    }
}
