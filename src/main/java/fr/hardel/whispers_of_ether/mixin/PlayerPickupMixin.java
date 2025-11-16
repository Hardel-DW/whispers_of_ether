package fr.hardel.whispers_of_ether.mixin;

import fr.hardel.whispers_of_ether.attributes.ModAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class PlayerPickupMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"))
    private AABB expandPickupBox(AABB aabb, double x, double y, double z) {
        Player player = (Player) (Object) this;
        double pickupAreaSize = player.getAttributeValue(ModAttribute.PICKUP_AREA_SIZE);
        return aabb.inflate(pickupAreaSize, 0.5 * pickupAreaSize, pickupAreaSize);
    }
}
