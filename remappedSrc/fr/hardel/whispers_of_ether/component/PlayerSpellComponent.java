package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;

public class PlayerSpellComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final List<Identifier> spellIds = new ArrayList<>();
    private final Map<Identifier, Long> spellCooldowns = new HashMap<>();

    public PlayerSpellComponent(PlayerEntity player) {
        this.player = player;
    }

    public List<Identifier> getSpellIds() {
        return new ArrayList<>(spellIds);
    }

    public void addSpell(Identifier spellId) {
        if (spellIds.contains(spellId))
            return;

        if (SpellResourceReloadListener.getSpell(spellId) == null)
            return;

        spellIds.add(spellId);
        ModComponents.PLAYER_SPELL.sync(player);
    }

    public void removeSpell(Identifier spellId) {
        if (spellIds.remove(spellId)) {
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public boolean hasSpell(Identifier spellId) {
        return spellIds.contains(spellId);
    }

    public void clearSpells() {
        if (!spellIds.isEmpty()) {
            spellIds.clear();
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public void setCooldown(Identifier spellId, long endTimeMillis) {
        spellCooldowns.put(spellId, endTimeMillis);
        ModComponents.PLAYER_SPELL.sync(player);
    }

    public long getRemainingCooldown(Identifier spellId) {
        Long endTime = spellCooldowns.get(spellId);
        if (endTime == null) {
            return 0;
        }
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public boolean isOnCooldown(Identifier spellId) {
        return getRemainingCooldown(spellId) > 0;
    }

    public void cleanExpiredCooldowns() {
        long currentTime = System.currentTimeMillis();
        boolean changed = spellCooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        if (changed) {
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public Map<Identifier, Long> getCooldowns() {
        return new HashMap<>(spellCooldowns);
    }

    @Override
    public void readData(ReadView readView) {
        spellIds.clear();
        spellCooldowns.clear();

        readView.read("spells", Codec.STRING.listOf()).ifPresent(strings -> {
            strings.forEach(str -> {
                var id = Identifier.tryParse(str);
                if (id != null) {
                    spellIds.add(id);
                }
            });
        });

        var cooldownsViewOpt = readView.getOptionalReadView("cooldowns");
        if (cooldownsViewOpt.isPresent()) {
            var cooldownsView = cooldownsViewOpt.get();
            for (Identifier spellId : spellIds) {
                var endTimeOpt = cooldownsView.getOptionalLong(spellId.toString());
                if (endTimeOpt.isPresent() && endTimeOpt.get() > 0) {
                    spellCooldowns.put(spellId, endTimeOpt.get());
                }
            }
        }
    }

    @Override
    public void writeData(WriteView writeView) {
        var strings = spellIds.stream().map(Identifier::toString).toList();
        writeView.put("spells", Codec.STRING.listOf(), strings);

        if (!spellCooldowns.isEmpty()) {
            var cooldownsView = writeView.get("cooldowns");
            for (var entry : spellCooldowns.entrySet()) {
                cooldownsView.putLong(entry.getKey().toString(), entry.getValue());
            }
        }
    }
}