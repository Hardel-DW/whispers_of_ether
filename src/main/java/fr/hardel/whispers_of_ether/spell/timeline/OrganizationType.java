package fr.hardel.whispers_of_ether.spell.timeline;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public record OrganizationType<T extends OrganizationTimeline>(MapCodec<T> codec) {
    
    public static final RegistryKey<Registry<OrganizationType<?>>> REGISTRY_KEY = 
        RegistryKey.ofRegistry(Identifier.of(WhispersOfEther.MOD_ID, "organization_timeline_type"));
    
    public static final Registry<OrganizationType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final OrganizationType<SequentialOrganization> SEQUENTIAL = 
        new OrganizationType<>(SequentialOrganization.CODEC);
    
    public static final OrganizationType<ParallelOrganization> PARALLEL = 
        new OrganizationType<>(ParallelOrganization.CODEC);
    
    public static final OrganizationType<RandomOrganization> RANDOM = 
        new OrganizationType<>(RandomOrganization.CODEC);
    
    public static final OrganizationType<LoopOrganization> LOOP = 
        new OrganizationType<>(LoopOrganization.CODEC);
    
    public static final OrganizationType<GroupOrganization> GROUP = 
        new OrganizationType<>(GroupOrganization.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "sequential"), SEQUENTIAL);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "parallel"), PARALLEL);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "random"), RANDOM);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "loop"), LOOP);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "group"), GROUP);
    }
}