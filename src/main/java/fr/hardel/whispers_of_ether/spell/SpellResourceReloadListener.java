package fr.hardel.whispers_of_ether.spell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.Identifier;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpellResourceReloadListener implements PreparableReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, Spell> SPELLS = new HashMap<>();

    private CompletableFuture<Map<Identifier, JsonElement>> loadSpells(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, JsonElement> rawSpells = new HashMap<>();

            for (Identifier id : manager.listResources("spells", path -> path.getPath().endsWith(".json")).keySet()) {
                Optional<Resource> resource = manager.getResource(id);
                if (resource.isEmpty())
                    continue;

                try (InputStream stream = resource.get().open()) {
                    JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);
                    Identifier spellId = Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring("spells/".length(), id.getPath().length() - ".json".length()));
                    rawSpells.put(spellId, json);
                } catch (Exception e) {
                    WhispersOfEther.LOGGER.error("Error loading spell {}: {}", id, e.getMessage());
                }
            }

            return rawSpells;
        }, executor);
    }

    private CompletableFuture<Void> applySpells(Map<Identifier, JsonElement> prepared, HolderLookup.Provider registries, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            SPELLS.clear();
            RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registries);

            for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
                DataResult<Spell> result = Spell.CODEC.parse(ops, entry.getValue());
                if (result.error().isPresent()) {
                    WhispersOfEther.LOGGER.error("Failed to parse spell {}: {}", entry.getKey(), result.error().map(Object::toString).orElse("Unknown error"));
                    continue;
                }

                SPELLS.put(entry.getKey(), result.result().get());
                WhispersOfEther.LOGGER.info("Loaded spell: {}", entry.getKey());
            }
        }, executor);
    }

    public static Map<Identifier, Spell> getSpells() {
        return Map.copyOf(SPELLS);
    }

    public static Spell getSpell(Identifier id) {
        return SPELLS.get(id);
    }

    @Override
    public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
        ResourceManager manager = sharedState.resourceManager();
        HolderLookup.Provider registries = sharedState.get(ResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY);
        return loadSpells(manager, executor).thenCompose(preparationBarrier::wait).thenCompose(data -> applySpells(data, registries, executor2));
    }
}