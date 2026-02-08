package fr.hardel.whispers_of_ether.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class OmnivampirismComponent implements AutoSyncedComponent {
    private final Player player;
    private float totalHeal;
    private float healedSoFar;
    private long startTick;

    public OmnivampirismComponent(Player player) {
        this.player = player;
        this.totalHeal = 0.0f;
        this.healedSoFar = 0.0f;
        this.startTick = 0;
    }

    public void trigger(float healAmount) {
        this.totalHeal = healAmount;
        this.healedSoFar = 0.0f;
        this.startTick = player.level().getGameTime();
        ModComponents.OMNIVAMPIRISM.sync(player);
    }

    public void tick() {
        if (totalHeal <= 0.0f) {
            return;
        }

        long currentTick = player.level().getGameTime();
        long elapsedTicks = currentTick - startTick;

        if (elapsedTicks >= 60) {
            float remainingHeal = totalHeal - healedSoFar;
            if (remainingHeal > 0.0f) {
                player.heal(remainingHeal);
            }
            reset();
            return;
        }

        float targetHeal = totalHeal * (elapsedTicks / 60.0f);
        float healThisTick = targetHeal - healedSoFar;

        if (healThisTick > 0.0f) {
            player.heal(healThisTick);
            healedSoFar = targetHeal;
        }
    }

    private void reset() {
        this.totalHeal = 0.0f;
        this.healedSoFar = 0.0f;
        this.startTick = 0;
        ModComponents.OMNIVAMPIRISM.sync(player);
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
        this.totalHeal = tag.getFloat("total_heal");
        this.healedSoFar = tag.getFloat("healed_so_far");
        this.startTick = tag.getLong("start_tick");
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putFloat("total_heal", totalHeal);
        tag.putFloat("healed_so_far", healedSoFar);
        tag.putLong("start_tick", startTick);
    }
}
