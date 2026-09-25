package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.network.WhispersOfEtherPacket.MultiJump;
import fr.hardel.whispers_of_ether.core.particles.ModParticle;
import fr.hardel.whispers_of_ether.world.attribute.ModAttribute;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class MultiJumpImpl {

    public static void handle(MultiJump packet, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerPlayer player = context.player();
            if (player.onGround() || player.getAttributeValue(ModAttribute.MULTI_JUMP) <= 1) {
                return;
            }

            ServerLevel level = player.level();
            for (int ring = 0; ring < 3; ring++) {
                level.sendParticles(ModParticle.WAVE_JUMP, player.getX(), player.getY() + 0.5, player.getZ(), 0, ring, 0.0, 0.0, 1.0);
            }
            float pitch = 0.5f + level.getRandom().nextFloat() * 1.5f;
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BREEZE_SLIDE, SoundSource.PLAYERS, 0.1f, pitch);
        });
    }
}
