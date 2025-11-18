package fr.hardel.whispers_of_ether.component;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public void readData(ValueInput readView) {
        this.totalHeal = readView.read("total_heal", Codec.FLOAT).orElse(0.0f);
        this.healedSoFar = readView.read("healed_so_far", Codec.FLOAT).orElse(0.0f);
        this.startTick = readView.getLong("start_tick").orElse(0L);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.store("total_heal", Codec.FLOAT, totalHeal);
        writeView.store("healed_so_far", Codec.FLOAT, healedSoFar);
        writeView.putLong("start_tick", startTick);
    }
}
