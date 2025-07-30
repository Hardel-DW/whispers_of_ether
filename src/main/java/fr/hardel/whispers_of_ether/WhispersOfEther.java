package fr.hardel.whispers_of_ether;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;

public class WhispersOfEther implements ModInitializer {

    public static final String MOD_ID = "whispers_of_ether";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Whispers of Ether");
        ModAttribute.registerAttributes();
    }
}
