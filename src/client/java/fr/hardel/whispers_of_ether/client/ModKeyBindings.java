package fr.hardel.whispers_of_ether.client.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import com.mojang.blaze3d.platform.InputConstants;

import fr.hardel.whispers_of_ether.WhispersOfEther;

import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
        private static final KeyMapping.Category MAIN_CATEGORY = KeyMapping.Category
                .register(Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "main"));
        public static final KeyMapping CAST_SPELL = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.whispers_of_ether.cast_spell", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C,
                MAIN_CATEGORY));

        public static final KeyMapping SPELL_UP = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.whispers_of_ether.spell_up", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP,
                MAIN_CATEGORY));

        public static final KeyMapping SPELL_DOWN = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.whispers_of_ether.spell_down", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN,
                MAIN_CATEGORY));

        public static void register() {
                // Les keybindings sont enregistrées automatiquement par
                // KeyBindingHelper.registerKeyBinding()
        }
}