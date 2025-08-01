package fr.hardel.whispers_of_ether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import fr.hardel.whispers_of_ether.command.SpellCommand;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import fr.hardel.whispers_of_ether.spell.action.ActionType;
import fr.hardel.whispers_of_ether.spell.target.TargetType;
import fr.hardel.whispers_of_ether.spell.target.position.PositionTargetType;
import fr.hardel.whispers_of_ether.spell.target.shape.ShapeType;
import fr.hardel.whispers_of_ether.spell.timeline.OrganizationType;
import fr.hardel.whispers_of_ether.spell.timeline.offset.LoopOffsetType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class WhispersOfEther implements ModInitializer {

    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Whispers of Ether");
        ModAttribute.registerAttributes();
        OrganizationType.register();
        ActionType.register();
        TargetType.register();
        ShapeType.register();
        PositionTargetType.register();
        LoopOffsetType.register();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SpellResourceReloadListener());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SpellResourceReloadListener.setRegistryLookup(registryAccess);
            SpellCommand.register(dispatcher);
        });
        NetworkHandler.registerServerPackets();
    }
}
