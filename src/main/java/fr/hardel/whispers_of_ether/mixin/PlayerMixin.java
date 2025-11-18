package fr.hardel.whispers_of_ether.mixin;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import fr.hardel.whispers_of_ether.component.ModComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"))
    private AABB expandPickupBox(AABB aabb, double x, double y, double z) {
        Player player = (Player) (Object) this;
        double pickupAreaSize = player.getAttributeValue(ModAttribute.PICKUP_AREA_SIZE);
        return aabb.inflate(pickupAreaSize, 0.5 * pickupAreaSize, pickupAreaSize);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 0), ordinal = 3)
    private float applyCriticalDamage(float totalDamage) {
        Player player = (Player) (Object) this;

        double critRate = player.getAttributeValue(ModAttribute.CRIT_RATE) - 1.0;
        if (critRate > 0.0 && player.getRandom().nextFloat() < critRate) {
            double critDamage = player.getAttributeValue(ModAttribute.CRIT_DAMAGE) - 1.0;
            totalDamage *= (1.0f + (float) critDamage);
        }

        return totalDamage;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void updateOmnivampirism(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!player.level().isClientSide()) {
            ModComponents.OMNIVAMPIRISM.get(player).tick();
        }
    }
}