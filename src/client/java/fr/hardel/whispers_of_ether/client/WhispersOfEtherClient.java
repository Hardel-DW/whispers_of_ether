package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.gui.screen.RunicTableScreen;
import fr.hardel.whispers_of_ether.client.gui.screen.RunicForgeScreen;
import fr.hardel.whispers_of_ether.client.gui.screen.RunicInfuserScreen;
import fr.hardel.whispers_of_ether.client.network.ClientNetworkHandler;
import fr.hardel.whispers_of_ether.client.particles.ModParticleClient;
import fr.hardel.whispers_of_ether.client.render.RenderSystem;
import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import fr.hardel.whispers_of_ether.item.ModItems;
import fr.hardel.whispers_of_ether.menu.ModMenuTypes;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import fr.hardel.whispers_of_ether.client.render.entity.DamageIndicatorRenderer;
import fr.hardel.whispers_of_ether.client.render.entity.TargetDummyRenderer;
import fr.hardel.whispers_of_ether.entity.ModEntities;
import fr.hardel.whispers_of_ether.client.render.entity.model.TargetDummyModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

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
        registerItemProperties();

        MenuScreens.register(ModMenuTypes.RUNIC_TABLE, RunicTableScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_FORGE, RunicForgeScreen::new);
        MenuScreens.register(ModMenuTypes.RUNIC_INFUSER, RunicInfuserScreen::new);
        EntityRendererRegistry.register(ModEntities.TARGET_DUMMY, TargetDummyRenderer::new);
        EntityRendererRegistry.register(ModEntities.DAMAGE_INDICATOR, DamageIndicatorRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(TargetDummyModel.LAYER_LOCATION, TargetDummyModel::createBodyLayer);

        WorldRenderEvents.AFTER_TRANSLUCENT.register(RenderSystem.getInstance()::renderAll);
        HudRenderCallback.EVENT.register(SpellSelector::render);
    }

    private static void registerItemProperties() {
        ResourceLocation tierId = ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "tier");

        registerRuneProperty(ModItems.RUNE_OF_ARMOR, tierId);
        registerRuneProperty(ModItems.RUNE_OF_ARMOR_TOUGHNESS, tierId);
        registerRuneProperty(ModItems.RUNE_OF_ATTACK_DAMAGE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_ATTACK_KNOCKBACK, tierId);
        registerRuneProperty(ModItems.RUNE_OF_ATTACK_SPEED, tierId);
        registerRuneProperty(ModItems.RUNE_OF_BLOCK_BREAK_SPEED, tierId);
        registerRuneProperty(ModItems.RUNE_OF_BLOCK_INTERACTION_RANGE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_BURNING_TIME, tierId);
        registerRuneProperty(ModItems.RUNE_OF_CRIT_DAMAGE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_CRIT_RATE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_ENTITY_INTERACTION_RANGE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_FALL_DAMAGE_MULTIPLIER, tierId);
        registerRuneProperty(ModItems.RUNE_OF_JUMP_STRENGTH, tierId);
        registerRuneProperty(ModItems.RUNE_OF_KNOCKBACK_RESISTANCE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_MAX_HEALTH, tierId);
        registerRuneProperty(ModItems.RUNE_OF_MINING_EFFICIENCY, tierId);
        registerRuneProperty(ModItems.RUNE_OF_MOVEMENT_SPEED, tierId);
        registerRuneProperty(ModItems.RUNE_OF_MULTI_JUMP, tierId);
        registerRuneProperty(ModItems.RUNE_OF_OMNIVAMPIRISM, tierId);
        registerRuneProperty(ModItems.RUNE_OF_OMNIVAMPIRISM_RATE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_PICKUP_AREA_SIZE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_SAFE_FALL_DISTANCE, tierId);
        registerRuneProperty(ModItems.RUNE_OF_SNEAKING_SPEED, tierId);
        registerRuneProperty(ModItems.RUNE_OF_STEP_HEIGHT, tierId);
        registerRuneProperty(ModItems.RUNE_OF_SWEEPING_DAMAGE_RATIO, tierId);
        registerRuneProperty(ModItems.RUNE_OF_WATER_MOVEMENT_EFFICIENCY, tierId);
    }

    private static void registerRuneProperty(Item item, ResourceLocation propertyId) {
        ItemProperties.register(item, propertyId, (stack, level, entity, seed) -> {
            RuneComponent rune = stack.get(ModItemComponent.RUNES);
            return rune != null ? rune.tier() / 5.0f : 0.0f;
        });
    }

    public static RenderSystem getRenderSystem() {
        return RenderSystem.getInstance();
    }
}
