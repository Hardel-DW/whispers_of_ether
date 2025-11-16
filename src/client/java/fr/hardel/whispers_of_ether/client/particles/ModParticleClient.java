package fr.hardel.whispers_of_ether.client.particles;

import fr.hardel.whispers_of_ether.particle.ModParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticleClient {
    public static void register() {
        ParticleFactoryRegistry.getInstance().register(ModParticle.WAVE_JUMP, WaveJump.Factory::new);
    }
}
