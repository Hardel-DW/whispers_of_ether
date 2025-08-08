package fr.hardel.whispers_of_ether.object;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class SceneObjectsComponents implements WorldComponentInitializer {
    public static final ComponentKey<SceneObjectsComponent> SCENE_OBJECTS = ComponentRegistry
            .getOrCreate(Identifier.of(WhispersOfEther.MOD_ID, "scene_objects"), SceneObjectsComponent.class);

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(SCENE_OBJECTS, SceneObjectsComponent::new);
    }
}
