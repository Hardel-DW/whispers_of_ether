package fr.hardel.whispers_of_ether.menu.slot;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RuneSlot extends Slot {
    public RuneSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.has(ModItemComponent.RUNES);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
