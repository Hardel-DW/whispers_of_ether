package fr.hardel.whispers_of_ether.spell.target.position;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public record PositionTargetType<T extends PositionTarget>(MapCodec<T> codec) {
    
    public static final RegistryKey<Registry<PositionTargetType<?>>> REGISTRY_KEY =
        RegistryKey.ofRegistry(Identifier.of(WhispersOfEther.MOD_ID, "position_target_type"));
    
    public static final Registry<PositionTargetType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final PositionTargetType<AbsolutePosition> ABSOLUTE = 
        new PositionTargetType<>(AbsolutePosition.CODEC);
    
    public static final PositionTargetType<RelativePosition> RELATIVE = 
        new PositionTargetType<>(RelativePosition.CODEC);
    
    public static final PositionTargetType<LocalPosition> LOCAL = 
        new PositionTargetType<>(LocalPosition.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "absolute"), ABSOLUTE);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "relative"), RELATIVE);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "local"), LOCAL);
    }
}