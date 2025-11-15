package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public record TargetType<T extends Target>(MapCodec<T> codec) {
    
    public static final RegistryKey<Registry<TargetType<?>>> REGISTRY_KEY =
        RegistryKey.ofRegistry(Identifier.of(WhispersOfEther.MOD_ID, "target_type"));
    
    public static final Registry<TargetType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final TargetType<SelfTarget> SELF = 
        new TargetType<>(SelfTarget.CODEC);
    
    public static final TargetType<AimedTarget> AIMED = 
        new TargetType<>(AimedTarget.CODEC);
    
    public static final TargetType<AreaTarget> AREA = 
        new TargetType<>(AreaTarget.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "self"), SELF);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "aimed"), AIMED);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "area"), AREA);
    }
}