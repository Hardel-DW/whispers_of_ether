package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ModComponents implements EntityComponentInitializer {
    
    public static final ComponentKey<PlayerSpellComponent> PLAYER_SPELL =
            ComponentRegistry.getOrCreate(Identifier.of(WhispersOfEther.MOD_ID, "player_spell"), PlayerSpellComponent.class);
    
    public static final ComponentKey<WaypointComponent> WAYPOINTS =
            ComponentRegistry.getOrCreate(Identifier.of(WhispersOfEther.MOD_ID, "waypoints"), WaypointComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, PLAYER_SPELL, player -> new PlayerSpellComponent(player));
        registry.registerFor(PlayerEntity.class, WAYPOINTS, player -> new WaypointComponent(player));
    }
}