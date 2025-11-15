package fr.hardel.whispers_of_ether.client.mixin;

import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import fr.hardel.whispers_of_ether.client.SpellCastHandler;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    private static long lastScrollTime = 0;
    private static final long SCROLL_COOLDOWN = 150; // milliseconds

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ModKeyBindings.CAST_SPELL.isDown()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastScrollTime >= SCROLL_COOLDOWN) {
                SpellSelector.setSelectedSlot(SpellSelector.getSelectedSlot() + (vertical > 0 ? -1 : 1));
                SpellCastHandler.notifyScrolled();
                lastScrollTime = currentTime;
            }
            ci.cancel();
        }
    }
}