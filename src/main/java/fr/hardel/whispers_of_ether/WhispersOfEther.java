package fr.hardel.whispers_of_ether;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.runic_attribute.AttributeDataLoader;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import fr.hardel.whispers_of_ether.block.ModBlocks;
import fr.hardel.whispers_of_ether.block.entity.ModBlockEntities;
import fr.hardel.whispers_of_ether.command.SpellCommand;
import fr.hardel.whispers_of_ether.recipe.ModRecipes;
import fr.hardel.whispers_of_ether.command.WaypointCommand;
import fr.hardel.whispers_of_ether.effects.ModEffects;
import fr.hardel.whispers_of_ether.entity.ModEntities;
import fr.hardel.whispers_of_ether.item.ModItems;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import fr.hardel.whispers_of_ether.menu.ModMenuTypes;
import fr.hardel.whispers_of_ether.particle.ModParticle;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import fr.hardel.whispers_of_ether.spell.action.ActionType;
import fr.hardel.whispers_of_ether.spell.target.TargetType;
import fr.hardel.whispers_of_ether.spell.target.position.PositionTargetType;
import fr.hardel.whispers_of_ether.spell.target.shape.ShapeType;
import fr.hardel.whispers_of_ether.spell.timeline.OrganizationType;
import fr.hardel.whispers_of_ether.spell.timeline.offset.LoopOffsetType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class WhispersOfEther implements ModInitializer {
    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModAttribute.register();
        ModEntities.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModItems.register();
        ModItemComponent.register();
        ModMenuTypes.register();
        ModRecipes.register();
        ModParticle.register();
        ItemGroupMod.register();
        OrganizationType.register();
        ActionType.register();
        TargetType.register();
        ShapeType.register();
        PositionTargetType.register();
        LoopOffsetType.register();
        ModEffects.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            SpellResourceReloadListener.setRegistryAccess(server.registryAccess());
            SpellResourceReloadListener.reloadSpells();
        });

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SpellResourceReloadListener());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new AttributeDataLoader());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SpellCommand.register(dispatcher);
            WaypointCommand.register(dispatcher);
        });

        NetworkHandler.registerServerPackets();
    }
}
