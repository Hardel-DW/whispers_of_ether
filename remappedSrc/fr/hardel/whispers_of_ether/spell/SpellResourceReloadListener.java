package fr.hardel.whispers_of_ether.spell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpellResourceReloadListener implements SimpleResourceReloadListener<Map<Identifier, JsonElement>> {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, Spell> SPELLS = new HashMap<>();
    private static final Map<Identifier, JsonElement> PENDING_SPELLS = new HashMap<>();
    private static MinecraftServer serverInstance;

    public static void setServerInstance(MinecraftServer server) {
        serverInstance = server;
        loadPendingSpells();
    }

    private static void loadPendingSpells() {
        if (serverInstance != null && !PENDING_SPELLS.isEmpty()) {
            WhispersOfEther.LOGGER.info("Loading {} pending spells with server registry context",
                    PENDING_SPELLS.size());
            RegistryWrapper.WrapperLookup registryLookup = serverInstance.getRegistryManager();
            var ops = RegistryOps.of(JsonOps.INSTANCE, registryLookup);

            for (Map.Entry<Identifier, JsonElement> entry : PENDING_SPELLS.entrySet()) {
                DataResult<Spell> result = Spell.CODEC.parse(ops, entry.getValue());
                if (result.result().isPresent()) {
                    SPELLS.put(entry.getKey(), result.result().get());
                    WhispersOfEther.LOGGER.info("Successfully loaded deferred spell: {}", entry.getKey());
                } else {
                    WhispersOfEther.LOGGER.error("Failed to parse deferred spell {}: {}", entry.getKey(),
                            result.error().map(Object::toString).orElse("Unknown error"));
                }
            }
            PENDING_SPELLS.clear();
        }
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(WhispersOfEther.MOD_ID, "spells");
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(
                ResourceReloadListenerKeys.RECIPES,
                ResourceReloadListenerKeys.FUNCTIONS,
                ResourceReloadListenerKeys.ADVANCEMENTS);
    }

    @Override
    public CompletableFuture<Map<Identifier, JsonElement>> load(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, JsonElement> rawSpells = new HashMap<>();

            for (Identifier id : manager.findResources("spells", path -> path.getPath().endsWith(".json")).keySet()) {
                Optional<Resource> resource = manager.getResource(id);
                if (resource.isEmpty())
                    continue;

                try (InputStream stream = resource.get().getInputStream()) {
                    JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);
                    Identifier spellId = Identifier.of(id.getNamespace(),
                            id.getPath().substring("spells/".length(), id.getPath().length() - ".json".length()));
                    rawSpells.put(spellId, json);
                } catch (Exception e) {
                    WhispersOfEther.LOGGER.error("Error loading spell {}: {}", id, e.getMessage());
                }
            }

            return rawSpells;
        }, executor);
    }

    @Override
    public CompletableFuture<Void> apply(Map<Identifier, JsonElement> prepared, ResourceManager manager,
                                         Executor executor) {
        return CompletableFuture.runAsync(() -> {
            SPELLS.clear();

            if (serverInstance != null) {
                RegistryWrapper.WrapperLookup registryLookup = serverInstance.getRegistryManager();
                var ops = RegistryOps.of(JsonOps.INSTANCE, registryLookup);

                for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
                    DataResult<Spell> result = Spell.CODEC.parse(ops, entry.getValue());
                    if (result.result().isPresent()) {
                        SPELLS.put(entry.getKey(), result.result().get());
                        WhispersOfEther.LOGGER.info("Loaded spell: {}", entry.getKey());
                    } else {
                        WhispersOfEther.LOGGER.error("Failed to parse spell {}: {}", entry.getKey(),
                                result.error().map(Object::toString).orElse("Unknown error"));
                    }
                }
            } else {
                WhispersOfEther.LOGGER.warn("Server instance not available, storing {} spells for deferred loading",
                        prepared.size());
                PENDING_SPELLS.clear();
                PENDING_SPELLS.putAll(prepared);
            }
        }, executor);
    }

    public static Map<Identifier, Spell> getSpells() {
        return Map.copyOf(SPELLS);
    }

    public static Spell getSpell(Identifier id) {
        return SPELLS.get(id);
    }
}