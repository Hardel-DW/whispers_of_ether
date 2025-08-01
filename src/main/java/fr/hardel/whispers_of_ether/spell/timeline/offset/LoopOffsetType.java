package fr.hardel.whispers_of_ether.spell.timeline.offset;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public record LoopOffsetType<T extends LoopOffset>(MapCodec<T> codec) {
    
    public static final RegistryKey<Registry<LoopOffsetType<?>>> REGISTRY_KEY = 
        RegistryKey.ofRegistry(Identifier.of(WhispersOfEther.MOD_ID, "loop_offset_type"));
    
    public static final Registry<LoopOffsetType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final LoopOffsetType<RandomBoxOffset> RANDOM_BOX = 
        new LoopOffsetType<>(RandomBoxOffset.CODEC);
    
    public static final LoopOffsetType<LookupOffset> LOOKUP = 
        new LoopOffsetType<>(LookupOffset.CODEC);
    
    public static final LoopOffsetType<RandomValueOffset> RANDOM_VALUE = 
        new LoopOffsetType<>(RandomValueOffset.CODEC);
    
    public static final LoopOffsetType<ForwardOffset> FORWARD = 
        new LoopOffsetType<>(ForwardOffset.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "random_box"), RANDOM_BOX);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "lookup"), LOOKUP);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "random_value"), RANDOM_VALUE);
        Registry.register(REGISTRY, Identifier.of(WhispersOfEther.MOD_ID, "forward"), FORWARD);
    }
}