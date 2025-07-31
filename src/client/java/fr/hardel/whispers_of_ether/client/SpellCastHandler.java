package fr.hardel.whispers_of_ether.client;

import fr.hardel.whispers_of_ether.client.keybind.ModKeyBindings;
import fr.hardel.whispers_of_ether.client.screen.SpellSelector;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SpellCastHandler {
    private static boolean wasPressed = false;
    private static boolean holdLogSent = false;
    private static boolean scrolledDuringHold = false;
    private static boolean wasSpellUpPressed = false;
    private static boolean wasSpellDownPressed = false;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean isPressed = ModKeyBindings.CAST_SPELL.isPressed();
            boolean isSpellUpPressed = ModKeyBindings.SPELL_UP.isPressed();
            boolean isSpellDownPressed = ModKeyBindings.SPELL_DOWN.isPressed();

            if (isPressed && !wasPressed) {
                holdLogSent = false;
                scrolledDuringHold = false;
                SpellSelector.showHUD();
            }

            if (!isPressed && wasPressed && !holdLogSent && !scrolledDuringHold) {
                System.out.println("Spell cast released - Casting spell!");
            }

            if (isSpellUpPressed && !wasSpellUpPressed) {
                SpellSelector.setSelectedSlot(SpellSelector.getSelectedSlot() - 1);
                if (isPressed) {
                    scrolledDuringHold = true;
                }
            }

            if (isSpellDownPressed && !wasSpellDownPressed) {
                SpellSelector.setSelectedSlot(SpellSelector.getSelectedSlot() + 1);
                if (isPressed) {
                    scrolledDuringHold = true;
                }
            }

            wasPressed = isPressed;
            wasSpellUpPressed = isSpellUpPressed;
            wasSpellDownPressed = isSpellDownPressed;
        });

    }

    public static void notifyScrolled() {
        scrolledDuringHold = true;
    }
}