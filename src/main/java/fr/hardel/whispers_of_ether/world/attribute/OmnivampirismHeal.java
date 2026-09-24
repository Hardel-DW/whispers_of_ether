package fr.hardel.whispers_of_ether.world.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

// Heal granted by omnivampirism, spread over DURATION ticks.
public record OmnivampirismHeal(float totalHeal, float healedSoFar, long startTick) {
    private static final int DURATION = 60;

    private static final Codec<OmnivampirismHeal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("total_heal").forGetter(OmnivampirismHeal::totalHeal),
        Codec.FLOAT.fieldOf("healed_so_far").forGetter(OmnivampirismHeal::healedSoFar),
        Codec.LONG.fieldOf("start_tick").forGetter(OmnivampirismHeal::startTick)).apply(instance, OmnivampirismHeal::new));

    private static final AttachmentType<OmnivampirismHeal> ATTACHMENT = AttachmentRegistry.createPersistent(
        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "omnivampirism"), CODEC);

    public static void register() {
        WhispersOfEther.LOGGER.info("Registering omnivampirism attachment for {}", WhispersOfEther.MOD_ID);
    }

    public static void start(Player player, float amount) {
        player.setAttached(ATTACHMENT, new OmnivampirismHeal(amount, 0.0f, player.level().getGameTime()));
    }

    public static void tick(Player player) {
        OmnivampirismHeal heal = player.getAttached(ATTACHMENT);
        if (heal != null) {
            heal.apply(player);
        }
    }

    private void apply(Player player) {
        long elapsedTicks = player.level().getGameTime() - startTick;
        float targetHeal = totalHeal * Math.min(1.0f, elapsedTicks / (float) DURATION);
        if (targetHeal > healedSoFar) {
            player.heal(targetHeal - healedSoFar);
        }

        if (elapsedTicks >= DURATION) {
            player.removeAttached(ATTACHMENT);
            return;
        }

        player.setAttached(ATTACHMENT, new OmnivampirismHeal(totalHeal, targetHeal, startTick));
    }
}
