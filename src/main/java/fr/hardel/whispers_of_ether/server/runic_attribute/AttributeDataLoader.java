package fr.hardel.whispers_of_ether.runic_attribute;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AttributeDataLoader implements PreparableReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, AttributeData> ATTRIBUTES = new HashMap<>();

    private CompletableFuture<Map<Identifier, JsonElement>> loadAttributes(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<Identifier, JsonElement> rawAttributes = new HashMap<>();

            for (Identifier id : manager.listResources("rune", path -> path.getPath().endsWith(".json")).keySet()) {
                Optional<Resource> resource = manager.getResource(id);
                if (resource.isEmpty())
                    continue;

                try (InputStream stream = resource.get().open()) {
                    JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);
                    Identifier runeId = Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring("rune/".length(), id.getPath().length() - ".json".length()));
                    rawAttributes.put(runeId, json);
                } catch (Exception e) {
                    WhispersOfEther.LOGGER.error("Error loading rune {}: {}", id, e.getMessage());
                }
            }

            return rawAttributes;
        }, executor);
    }

    private CompletableFuture<Void> applyAttributes(Map<Identifier, JsonElement> prepared, HolderLookup.Provider registries, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            ATTRIBUTES.clear();
            RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registries);

            for (Map.Entry<Identifier, JsonElement> entry : prepared.entrySet()) {
                DataResult<AttributeData> result = AttributeData.CODEC.parse(ops, entry.getValue());
                if (result.error().isPresent()) {
                    WhispersOfEther.LOGGER.error("Failed to parse attribute {}: {}", entry.getKey(), result.error().map(Object::toString).orElse("Unknown error"));
                    continue;
                }

                result.result().ifPresent(data -> ATTRIBUTES.put(entry.getKey(), data));
                WhispersOfEther.LOGGER.info("Loaded rune: {}", entry.getKey());
            }
        }, executor);
    }

    public static Map<Identifier, AttributeData> getAttributes() {
        return Map.copyOf(ATTRIBUTES);
    }

    public static Optional<AttributeData> getAttribute(Identifier id) {
        return Optional.ofNullable(ATTRIBUTES.get(id));
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
        ResourceManager manager = sharedState.resourceManager();
        HolderLookup.Provider registries = sharedState.get(ResourceLoader.RELOADER_REGISTRY_LOOKUP_KEY);
        return loadAttributes(manager, executor).thenCompose(preparationBarrier::wait).thenCompose(data -> applyAttributes(data, registries, executor2));
    }
}
