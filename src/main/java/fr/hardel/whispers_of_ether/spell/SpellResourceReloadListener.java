package fr.hardel.whispers_of_ether.spell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpellResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, Spell> SPELLS = new HashMap<>();
    private static RegistryWrapper.WrapperLookup registryLookup;

    public static void setRegistryLookup(RegistryWrapper.WrapperLookup lookup) {
        registryLookup = lookup;
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.of(WhispersOfEther.MOD_ID, "spells");
    }

    @Override
    public void reload(ResourceManager manager) {
        SPELLS.clear();

        for (Identifier id : manager.findResources("spells", path -> path.getPath().endsWith(".json")).keySet()) {
            Optional<Resource> resource = manager.getResource(id);
            if (resource.isEmpty())
                continue;

            try (InputStream stream = resource.get().getInputStream()) {
                JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);

                DynamicOps<JsonElement> ops = registryLookup != null ? RegistryOps.of(JsonOps.INSTANCE, registryLookup)
                        : JsonOps.INSTANCE;
                DataResult<Spell> result = Spell.CODEC.parse(ops, json);
                if (result.result().isPresent()) {
                    Identifier spellId = Identifier.of(id.getNamespace(),
                            id.getPath().substring("spells/".length(), id.getPath().length() - ".json".length()));
                    SPELLS.put(spellId, result.result().get());
                    WhispersOfEther.LOGGER.info("Loaded spell: {}", spellId);
                } else {
                    WhispersOfEther.LOGGER.error("Failed to parse spell {}: {}", id, result.error().orElse(null));
                }
            } catch (Exception e) {
                WhispersOfEther.LOGGER.error("Error loading spell {}: {}", id, e.getMessage());
            }
        }
    }

    public static Map<Identifier, Spell> getSpells() {
        return Map.copyOf(SPELLS);
    }

    public static Spell getSpell(Identifier id) {
        return SPELLS.get(id);
    }
}