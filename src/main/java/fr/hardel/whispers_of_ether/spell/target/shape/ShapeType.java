package fr.hardel.whispers_of_ether.spell.target.shape;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public record ShapeType<T extends Shape>(MapCodec<T> codec) {
    
    public static final ResourceKey<Registry<ShapeType<?>>> REGISTRY_KEY =
        ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "shape_type"));
    
    public static final Registry<ShapeType<?>> REGISTRY = 
        FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    
    public static final ShapeType<SphereShape> SPHERE = 
        new ShapeType<>(SphereShape.CODEC);
    
    public static final ShapeType<CubeShape> CUBE = 
        new ShapeType<>(CubeShape.CODEC);
    
    public static final ShapeType<BoxShape> BOX = 
        new ShapeType<>(BoxShape.CODEC);
    
    public static void register() {
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "sphere"), SPHERE);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "cube"), CUBE);
        Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "box"), BOX);
    }
}