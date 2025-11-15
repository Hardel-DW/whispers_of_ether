package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record PositionTargetType<T extends PositionTarget>(MapCodec<T> codec) {
    
    public static final ResourceKey<Registry<PositionTargetType<?>>> REGISTRY_KEY =
        ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "position_target_type"));
    
    public static final Registry<PositionTargetType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final PositionTargetType<AbsolutePosition> ABSOLUTE = 
        new PositionTargetType<>(AbsolutePosition.CODEC);
    
    public static final PositionTargetType<RelativePosition> RELATIVE = 
        new PositionTargetType<>(RelativePosition.CODEC);
    
    public static final PositionTargetType<LocalPosition> LOCAL = 
        new PositionTargetType<>(LocalPosition.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "absolute"), ABSOLUTE);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "relative"), RELATIVE);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "local"), LOCAL);
    }
}