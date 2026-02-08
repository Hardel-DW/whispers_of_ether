package fr.hardel.whispers_of_ether.spell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpellResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, Spell> SPELLS = new HashMap<>();

    private CompletableFuture<Map<ResourceLocation, JsonElement>> loadSpells(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ResourceLocation, JsonElement> rawSpells = new HashMap<>();

            for (ResourceLocation id : manager.listResources("spells", path -> path.getPath().endsWith(".json")).keySet()) {
                Optional<Resource> resource = manager.getResource(id);
                if (resource.isEmpty())
                    continue;

                try (InputStream stream = resource.get().open()) {
                    JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);
                    ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring("spells/".length(), id.getPath().length() - ".json".length()));
                    rawSpells.put(spellId, json);
                } catch (Exception e) {
                    WhispersOfEther.LOGGER.error("Error loading spell {}: {}", id, e.getMessage());
                }
            }

            return rawSpells;
        }, executor);
    }

    private CompletableFuture<Void> applySpells(Map<ResourceLocation, JsonElement> prepared, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            SPELLS.clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : prepared.entrySet()) {
                DataResult<Spell> result = Spell.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
                if (result.error().isPresent()) {
                    WhispersOfEther.LOGGER.error("Failed to parse spell {}: {}", entry.getKey(), result.error().map(Object::toString).orElse("Unknown error"));
                    continue;
                }

                result.result().ifPresent(spell -> SPELLS.put(entry.getKey(), spell));
                WhispersOfEther.LOGGER.info("Loaded spell: {}", entry.getKey());
            }
        }, executor);
    }

    public static Map<ResourceLocation, Spell> getSpells() {
        return Map.copyOf(SPELLS);
    }

    public static Spell getSpell(ResourceLocation id) {
        return SPELLS.get(id);
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "spells");
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return loadSpells(manager, prepareExecutor).thenCompose(barrier::wait).thenCompose(data -> applySpells(data, applyExecutor));
    }
}