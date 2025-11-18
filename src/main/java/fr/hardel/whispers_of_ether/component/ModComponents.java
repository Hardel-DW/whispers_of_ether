package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ModComponents implements EntityComponentInitializer {
    
    public static final ComponentKey<PlayerSpellComponent> PLAYER_SPELL =
            ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "player_spell"), PlayerSpellComponent.class);
    
    public static final ComponentKey<WaypointComponent> WAYPOINTS =
            ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "waypoints"), WaypointComponent.class);

    public static final ComponentKey<OmnivampirismComponent> OMNIVAMPIRISM =
            ComponentRegistry.getOrCreate(ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "omnivampirism"), OmnivampirismComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Player.class, PLAYER_SPELL, player -> new PlayerSpellComponent(player));
        registry.registerFor(Player.class, WAYPOINTS, player -> new WaypointComponent(player));
        registry.registerFor(Player.class, OMNIVAMPIRISM, player -> new OmnivampirismComponent(player));
    }
}