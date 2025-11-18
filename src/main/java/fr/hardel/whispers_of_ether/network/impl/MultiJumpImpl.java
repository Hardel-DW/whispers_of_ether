package fr.hardel.whispers_of_ether.network.impl;

import fr.hardel.whispers_of_ether.network.WhispersOfEtherPacket.MultiJump;
import fr.hardel.whispers_of_ether.particle.ModParticle;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class MultiJumpImpl {

    public static void handle(MultiJump packet, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerPlayer player = context.player();
            ServerLevel level = player.level();
            for (int i = 0; i < 3; i++) {
                level.sendParticles(ModParticle.WAVE_JUMP,
                        player.getX(), player.getY() + 0.5, player.getZ(),
                        1, 0.0, 0.0, 0.0, i);
            }
            float pitch = 0.5f + level.random.nextFloat() * 1.5f;
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BREEZE_SLIDE, SoundSource.PLAYERS, 0.1f, pitch);
        });
    }
}
