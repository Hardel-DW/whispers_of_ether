package fr.hardel.whispers_of_ether.client.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyBinding CAST_SPELL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.whispers_of_ether.cast_spell", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.whispers_of_ether"));

    public static void register() {
        // Les keybindings sont enregistrées automatiquement par
        // KeyBindingHelper.registerKeyBinding()
    }
}