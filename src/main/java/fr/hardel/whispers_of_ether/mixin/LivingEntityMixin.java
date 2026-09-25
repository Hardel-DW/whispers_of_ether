package fr.hardel.whispers_of_ether.mixin;

import fr.hardel.whispers_of_ether.world.attribute.ModAttribute;
import fr.hardel.whispers_of_ether.world.attribute.OmnivampirismHeal;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Unique
    private int whispers_of_ether$airJumps = 0;

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void countAirJump(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player && !self.onGround()) {
            whispers_of_ether$airJumps++;
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void resetAirJumps(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player && self.onGround()) {
            whispers_of_ether$airJumps = 0;
        }
    }

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z", ordinal = 2))
    private boolean allowAirJump(LivingEntity instance) {
        if (instance instanceof Player && !instance.onGround()) {
            return whispers_of_ether$airJumps < getAttributeValue(ModAttribute.MULTI_JUMP) - 1;
        }
        return instance.onGround();
    }

    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void triggerOmnivampirism(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || !(source.getEntity() instanceof Player attacker)) {
            return;
        }

        double omnivampRate = attacker.getAttributeValue(ModAttribute.OMNIVAMPIRISM_RATE) - 1.0;
        if (omnivampRate <= 0.0 || attacker.getRandom().nextFloat() >= omnivampRate) {
            return;
        }

        double omnivampValue = attacker.getAttributeValue(ModAttribute.OMNIVAMPIRISM) - 1.0;
        if (omnivampValue <= 0.0) {
            return;
        }

        float healAmount = damage * (float) omnivampValue;
        if (healAmount > 0.0f) {
            OmnivampirismHeal.start(attacker, healAmount);
        }
    }
}
