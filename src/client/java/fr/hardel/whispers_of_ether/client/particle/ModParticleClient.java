package fr.hardel.whispers_of_ether.client.particle;

import fr.hardel.whispers_of_ether.core.particles.ModParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticleClient {
    public static void register() {
        ParticleFactoryRegistry.getInstance().register(ModParticle.WAVE_JUMP, WaveJump.Factory::new);
    }
}
