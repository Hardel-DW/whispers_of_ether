package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.resources.Identifier;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerSpellComponent implements AutoSyncedComponent {
    private final Player player;
    private final List<Identifier> spellIds = new ArrayList<>();
    private final Map<Identifier, Long> spellCooldowns = new HashMap<>();

    public PlayerSpellComponent(Player player) {
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
    public void readData(ValueInput readView) {
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

        var cooldownsViewOpt = readView.child("cooldowns");
        if (cooldownsViewOpt.isPresent()) {
            var cooldownsView = cooldownsViewOpt.get();
            for (Identifier spellId : spellIds) {
                var endTimeOpt = cooldownsView.getLong(spellId.toString());
                if (endTimeOpt.isPresent() && endTimeOpt.get() > 0) {
                    spellCooldowns.put(spellId, endTimeOpt.get());
                }
            }
        }
    }

    @Override
    public void writeData(ValueOutput writeView) {
        var strings = spellIds.stream().map(Identifier::toString).toList();
        writeView.store("spells", Codec.STRING.listOf(), strings);

        if (!spellCooldowns.isEmpty()) {
            var cooldownsView = writeView.child("cooldowns");
            for (var entry : spellCooldowns.entrySet()) {
                cooldownsView.putLong(entry.getKey().toString(), entry.getValue());
            }
        }
    }
}