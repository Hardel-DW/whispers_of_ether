package fr.hardel.whispers_of_ether.mixin;

import fr.hardel.whispers_of_ether.MultiJumpAccessor;
import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityJumpMixin implements MultiJumpAccessor {

    @Shadow
    public abstract double getAttributeValue(
            net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute);

    @Unique
    private int whispers_of_ether$jumpCount = 0;

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player) {
            whispers_of_ether$jumpCount++;
        }
    }

    @Override
    public int whispers_of_ether$getJumpCount() {
        return whispers_of_ether$jumpCount;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void resetJumpCount(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player && self.onGround()) {
            whispers_of_ether$jumpCount = 0;
        }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z", ordinal = 2))
    private boolean allowMultiJump(LivingEntity instance) {
        boolean actualOnGround = instance.onGround();
        if (instance instanceof Player && !actualOnGround) {
            int maxJumps = (int) getAttributeValue(ModAttribute.MULTI_JUMP);
            return whispers_of_ether$jumpCount < maxJumps;
        }
        return actualOnGround;
    }
}
