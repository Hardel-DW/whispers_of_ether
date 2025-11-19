package fr.hardel.whispers_of_ether;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import fr.hardel.whispers_of_ether.command.SpellCommand;
import fr.hardel.whispers_of_ether.command.WaypointCommand;
import fr.hardel.whispers_of_ether.command.EtherObjectCommand;
import fr.hardel.whispers_of_ether.effects.ModEffects;
import fr.hardel.whispers_of_ether.entity.ModEntities;
import fr.hardel.whispers_of_ether.item.ModItems;
import fr.hardel.whispers_of_ether.itemgroup.ItemGroupMod;
import fr.hardel.whispers_of_ether.particle.ModParticle;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import fr.hardel.whispers_of_ether.object.SceneObjectType;
import fr.hardel.whispers_of_ether.object.SceneObjectTypes;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import fr.hardel.whispers_of_ether.spell.action.ActionType;
import fr.hardel.whispers_of_ether.spell.target.TargetType;
import fr.hardel.whispers_of_ether.spell.target.position.PositionTargetType;
import fr.hardel.whispers_of_ether.spell.target.shape.ShapeType;
import fr.hardel.whispers_of_ether.spell.timeline.OrganizationType;
import fr.hardel.whispers_of_ether.spell.timeline.offset.LoopOffsetType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class WhispersOfEther implements ModInitializer {

    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Whispers of Ether");
        ModAttribute.register();
        ModEntities.register();
        ModItems.register();
        ModItemComponent.register();
        ModParticle.register();
        ItemGroupMod.register();
        OrganizationType.register();
        ActionType.register();
        TargetType.register();
        ShapeType.register();
        PositionTargetType.register();
        LoopOffsetType.register();
        SceneObjectType.register();
        SceneObjectTypes.register();
        ModEffects.register();

        ResourceLoader.get(PackType.SERVER_DATA)
                .registerReloader(ResourceLocation.fromNamespaceAndPath(MOD_ID, "spells"),
                        new SpellResourceReloadListener());
        CommandRegistrationCallback.EVENT
                .register((dispatcher, registryAccess, environment) -> {
                    SpellCommand.register(dispatcher);
                    WaypointCommand.register(dispatcher);
                    EtherObjectCommand.register(dispatcher);
                });
        NetworkHandler.registerServerPackets();
    }
}
