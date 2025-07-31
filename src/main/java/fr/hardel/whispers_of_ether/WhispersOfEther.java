package fr.hardel.whispers_of_ether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import fr.hardel.whispers_of_ether.command.SpellCommand;
import fr.hardel.whispers_of_ether.network.NetworkHandler;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
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
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
            .registerReloadListener(new SpellResourceReloadListener());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            SpellCommand.register(dispatcher));
        NetworkHandler.registerServerPackets();
    }
}
