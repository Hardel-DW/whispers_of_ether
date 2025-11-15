package fr.hardel.whispers_of_ether.spell.target;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record TargetType<T extends Target>(MapCodec<T> codec) {
    
    public static final ResourceKey<Registry<TargetType<?>>> REGISTRY_KEY =
        ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "target_type"));
    
    public static final Registry<TargetType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final TargetType<SelfTarget> SELF = 
        new TargetType<>(SelfTarget.CODEC);
    
    public static final TargetType<AimedTarget> AIMED = 
        new TargetType<>(AimedTarget.CODEC);
    
    public static final TargetType<AreaTarget> AREA = 
        new TargetType<>(AreaTarget.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "self"), SELF);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "aimed"), AIMED);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "area"), AREA);
    }
}