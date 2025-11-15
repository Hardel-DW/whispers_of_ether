package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.ComponentFactory;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;
import net.minecraft.world.level.Level;

public class SceneObjectsComponents implements WorldComponentInitializer {
    public static final ComponentKey<SceneObjectsComponent> SCENE_OBJECTS = ComponentRegistry
            .getOrCreate(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "scene_objects"), SceneObjectsComponent.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        ComponentFactory<Level, SceneObjectsComponent> factory = SceneObjectsComponent::new;
        registry.register(SCENE_OBJECTS, factory);
    }
}
