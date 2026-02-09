package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public record ActionType<T extends Action>(MapCodec<T> codec) {

    public static final ResourceKey<Registry<ActionType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "action_type"));

    public static final Registry<ActionType<?>> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();

    public static final ActionType<SummonAction> SUMMON = new ActionType<>(SummonAction.CODEC);

    public static final ActionType<DamageAction> DAMAGE = new ActionType<>(DamageAction.CODEC);

    public static final ActionType<IceSpikesAction> ICE_SPIKES = new ActionType<>(IceSpikesAction.CODEC);

    public static final ActionType<ParticleAction> PARTICLE = new ActionType<>(ParticleAction.CODEC);

    public static void register() {
        Registry.register(REGISTRY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "summon"), SUMMON);
        Registry.register(REGISTRY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "damage"), DAMAGE);
        Registry.register(REGISTRY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "ice_spikes"), ICE_SPIKES);
        Registry.register(REGISTRY, Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "particle"), PARTICLE);
    }
}