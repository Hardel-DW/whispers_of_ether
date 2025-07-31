package fr.hardel.whispers_of_ether.spell;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SpellResourceReloadListener implements SimpleSynchronousResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, Spell> SPELLS = new HashMap<>();

    @Override
    public Identifier getFabricId() {
        return Identifier.of(WhispersOfEther.MOD_ID, "spells");
    }

    @Override
    public void reload(ResourceManager manager) {
        SPELLS.clear();

        for (Identifier id : manager.findResources("spells", path -> path.getPath().endsWith(".json")).keySet()) {
            var resource = manager.getResource(id);
            if (resource.isEmpty())
                continue;

            try (InputStream stream = resource.get().getInputStream()) {
                JsonElement json = GSON.fromJson(new InputStreamReader(stream), JsonElement.class);

                var result = Spell.CODEC.parse(JsonOps.INSTANCE, json);
                if (result.result().isPresent()) {
                    Identifier spellId = Identifier.of(id.getNamespace(),
                            id.getPath().substring("spells/".length(), id.getPath().length() - ".json".length()));
                    SPELLS.put(spellId, result.result().get());
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