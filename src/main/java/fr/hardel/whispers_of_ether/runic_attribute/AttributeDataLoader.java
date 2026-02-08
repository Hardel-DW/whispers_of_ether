package fr.hardel.whispers_of_ether.runic_attribute;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
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

public class AttributeDataLoader implements IdentifiableResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, AttributeData> ATTRIBUTES = new HashMap<>();

    private CompletableFuture<Map<ResourceLocation, JsonElement>> loadAttributes(ResourceManager manager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ResourceLocation, JsonElement> rawAttributes = new HashMap<>();

            for (ResourceLocation id : manager.listResources("rune", path -> path.getPath().endsWith(".json")).keySet()) {
                Optional<Resource> resource = manager.getResource(id);
                if (resource.isEmpty())
                    continue;

                try (InputStream stream = resource.get().open()) {
                    JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);
                    ResourceLocation runeId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring("rune/".length(), id.getPath().length() - ".json".length()));
                    rawAttributes.put(runeId, json);
                } catch (Exception e) {
                    WhispersOfEther.LOGGER.error("Error loading rune {}: {}", id, e.getMessage());
                }
            }

            return rawAttributes;
        }, executor);
    }

    private CompletableFuture<Void> applyAttributes(Map<ResourceLocation, JsonElement> prepared, Executor executor) {
        return CompletableFuture.runAsync(() -> {
            ATTRIBUTES.clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : prepared.entrySet()) {
                DataResult<AttributeData> result = AttributeData.CODEC.parse(JsonOps.INSTANCE, entry.getValue());
                if (result.error().isPresent()) {
                    WhispersOfEther.LOGGER.error("Failed to parse attribute {}: {}", entry.getKey(), result.error().map(Object::toString).orElse("Unknown error"));
                    continue;
                }

                result.result().ifPresent(data -> ATTRIBUTES.put(entry.getKey(), data));
                WhispersOfEther.LOGGER.info("Loaded rune: {}", entry.getKey());
            }
        }, executor);
    }

    public static Map<ResourceLocation, AttributeData> getAttributes() {
        return Map.copyOf(ATTRIBUTES);
    }

    public static Optional<AttributeData> getAttribute(ResourceLocation id) {
        return Optional.ofNullable(ATTRIBUTES.get(id));
    }

    @Override
    public ResourceLocation getFabricId() {
        return ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "rune");
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor prepareExecutor, Executor applyExecutor) {
        return loadAttributes(manager, prepareExecutor).thenCompose(barrier::wait).thenCompose(data -> applyAttributes(data, applyExecutor));
    }
}
