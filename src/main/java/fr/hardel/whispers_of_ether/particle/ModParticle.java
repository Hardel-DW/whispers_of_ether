package fr.hardel.whispers_of_ether.particle;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModParticle {
    public static final SimpleParticleType WAVE_JUMP = register("wave_jump", FabricParticleTypes.simple());

    private static SimpleParticleType register(String id, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, id), particleType);
    }

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering Mod Particles for {}", WhispersOfEther.MOD_ID);
    }
}
