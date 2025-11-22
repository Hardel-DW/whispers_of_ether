package fr.hardel.whispers_of_ether.menu;

import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.menu.slot.EquipmentSlot;
import fr.hardel.whispers_of_ether.menu.slot.RuneSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ForgeMagicMenu extends AbstractContainerMenu {
    private static final int RUNE_SLOT = 0;
    private static final int EQUIPMENT_SLOT = 1;
    private static final int CONTAINER_SIZE = 2;
    private boolean isProcessing = false;

    private final Container container = new SimpleContainer(CONTAINER_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            ForgeMagicMenu.this.slotsChanged(this);
        }
    };

    public ForgeMagicMenu(int containerId, Inventory playerInventory) {
        super(ModMenuTypes.FORGE_MAGIC, containerId);
        container.startOpen(playerInventory.player);
        addSlot(new RuneSlot(container, RUNE_SLOT, 133, 47));
        addSlot(new EquipmentSlot(container, EQUIPMENT_SLOT, 232, 47));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 111 + col * 18, 92 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 111 + col * 18, 150));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack result = stack.copy();

        if (index < CONTAINER_SIZE) {
            if (!moveItemStackTo(stack, CONTAINER_SIZE, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.has(ModItemComponent.RUNES)) {
            if (!moveItemStackTo(stack, RUNE_SLOT, RUNE_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, EQUIPMENT_SLOT, EQUIPMENT_SLOT + 1, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
        clearContainer(player, container);
    }

    public ItemStack getRuneStack() {
        return container.getItem(RUNE_SLOT);
    }

    public ItemStack getEquipmentStack() {
        return container.getItem(EQUIPMENT_SLOT);
    }

    @Override
    public void slotsChanged(Container container) {
        if (isProcessing) {
            return;
        }
        super.slotsChanged(container);
        System.out.println("[DEBUG] slotsChanged called! container=" + container + ", this.container=" + this.container
                + ", equals=" + (container == this.container));
        if (container != this.container) {
            System.out.println("[DEBUG] Container mismatch, returning.");
            return;
        }

        ItemStack runeStack = getRuneStack();
        ItemStack equipmentStack = getEquipmentStack();

        if (runeStack.isEmpty() || equipmentStack.isEmpty()) {
            return;
        }

        if (!runeStack.has(ModItemComponent.RUNES)) {
            System.out.println("[FORGE] Rune stack has no RUNES component!");
            return;
        }

        System.out.println("[FORGE] Attempting to apply rune...");
        isProcessing = true;
        try {
            applyRune(runeStack, equipmentStack);
        } finally {
            isProcessing = false;
        }
    }

    private void applyRune(ItemStack runeStack, ItemStack equipmentStack) {
        RuneForgeLogic.ForgeResult result = RuneForgeLogic.applyRune(runeStack, equipmentStack);
        System.out.println("[FORGE] Result: " + result.outcome() + " - " + result.message());

        if (result.outcome() == RuneForgeLogic.Outcome.BLOCKED) {
            return;
        }

        container.setItem(EQUIPMENT_SLOT, result.resultStack());
        runeStack.shrink(1);
        container.setChanged();
        System.out.println("[FORGE] Success! Rune consumed.");
    }
}
