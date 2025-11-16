package fr.hardel.whispers_of_ether.client.mixin;

import fr.hardel.whispers_of_ether.MultiJumpAccessor;
import fr.hardel.whispers_of_ether.network.WhispersOfEtherPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityJumpClientMixin {

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void onClientJump(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player && self.level().isClientSide()) {
            int jumpCount = ((MultiJumpAccessor) self).whispers_of_ether$getJumpCount();
            if (jumpCount > 1) {
                ClientPlayNetworking.send(new WhispersOfEtherPacket.MultiJump());
            }
        }
    }
}
