package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;
import fr.hardel.whispers_of_ether.spell.SpellResourceReloadListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.ArrayList;
import java.util.List;

public class PlayerSpellComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    private final List<Identifier> spellIds = new ArrayList<>();

    public PlayerSpellComponent(PlayerEntity player) {
        this.player = player;
    }

    public List<Identifier> getSpellIds() {
        return new ArrayList<>(spellIds);
    }

    public void addSpell(Identifier spellId) {
        if (spellIds.contains(spellId)) return;
        
        if (SpellResourceReloadListener.getSpell(spellId) == null) return;
        
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

    @Override
    public void readData(ReadView readView) {
        spellIds.clear();
        readView.read("spells", Codec.STRING.listOf()).ifPresent(strings -> {
            strings.forEach(str -> {
                var id = Identifier.tryParse(str);
                if (id != null) {
                    spellIds.add(id);
                }
            });
        });
    }

    @Override
    public void writeData(WriteView writeView) {
        var strings = spellIds.stream().map(Identifier::toString).toList();
        writeView.put("spells", Codec.STRING.listOf(), strings);
    }
}