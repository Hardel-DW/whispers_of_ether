package fr.hardel.whispers_of_ether.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhispersOfEtherDataGenerator implements DataGeneratorEntrypoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(WhispersOfEtherClient.MOD_ID);

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        LOGGER.info("Initializing Whispers of Ether data generator", pack);
    }
}
