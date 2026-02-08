package fr.hardel.whispers_of_ether.component;

import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerSpellComponent implements AutoSyncedComponent {
    private final Player player;
    private final List<ResourceLocation> spellIds = new ArrayList<>();
    private final Map<ResourceLocation, Long> spellCooldowns = new HashMap<>();

    public PlayerSpellComponent(Player player) {
        this.player = player;
    }

    public List<ResourceLocation> getSpellIds() {
        return new ArrayList<>(spellIds);
    }

    public void addSpell(ResourceLocation spellId) {
        if (spellIds.contains(spellId))
            return;

        if (SpellResourceReloadListener.getSpell(spellId) == null)
            return;

        spellIds.add(spellId);
        ModComponents.PLAYER_SPELL.sync(player);
    }

    public void removeSpell(ResourceLocation spellId) {
        if (spellIds.remove(spellId)) {
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public boolean hasSpell(ResourceLocation spellId) {
        return spellIds.contains(spellId);
    }

    public void clearSpells() {
        if (!spellIds.isEmpty()) {
            spellIds.clear();
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public void setCooldown(ResourceLocation spellId, long endTimeMillis) {
        spellCooldowns.put(spellId, endTimeMillis);
        ModComponents.PLAYER_SPELL.sync(player);
    }

    public long getRemainingCooldown(ResourceLocation spellId) {
        Long endTime = spellCooldowns.get(spellId);
        if (endTime == null) {
            return 0;
        }
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public boolean isOnCooldown(ResourceLocation spellId) {
        return getRemainingCooldown(spellId) > 0;
    }

    public void cleanExpiredCooldowns() {
        long currentTime = System.currentTimeMillis();
        boolean changed = spellCooldowns.entrySet().removeIf(entry -> entry.getValue() <= currentTime);
        if (changed) {
            ModComponents.PLAYER_SPELL.sync(player);
        }
    }

    public Map<ResourceLocation, Long> getCooldowns() {
        return new HashMap<>(spellCooldowns);
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
        spellIds.clear();
        spellCooldowns.clear();

        ListTag list = tag.getList("spells", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            var id = ResourceLocation.tryParse(list.getString(i));
            if (id != null) {
                spellIds.add(id);
            }
        }

        CompoundTag cooldowns = tag.getCompound("cooldowns");
        for (ResourceLocation spellId : spellIds) {
            String key = spellId.toString();
            if (cooldowns.contains(key)) {
                long endTime = cooldowns.getLong(key);
                if (endTime > 0) {
                    spellCooldowns.put(spellId, endTime);
                }
            }
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        spellIds.forEach(id -> list.add(StringTag.valueOf(id.toString())));
        tag.put("spells", list);

        if (!spellCooldowns.isEmpty()) {
            CompoundTag cooldowns = new CompoundTag();
            spellCooldowns.forEach((key, value) -> cooldowns.putLong(key.toString(), value));
            tag.put("cooldowns", cooldowns);
        }
    }
}