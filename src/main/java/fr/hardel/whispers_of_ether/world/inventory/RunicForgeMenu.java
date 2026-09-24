package fr.hardel.whispers_of_ether.world.inventory;

import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RunicForgeMenu extends AbstractContainerMenu {
    private static final int[][] CRAFT_SLOT_POSITIONS = { { 81, 12 }, { 52, 33 }, { 110, 33 }, { 62, 64 }, { 100, 64 } };

    private final Container container;
    private final ContainerData data;

    public RunicForgeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(RunicForgeBlockEntity.CONTAINER_SIZE), new SimpleContainerData(1));
    }

    public RunicForgeMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.RUNIC_FORGE, containerId);
        checkContainerSize(container, RunicForgeBlockEntity.CONTAINER_SIZE);
        this.container = container;
        this.data = data;
        this.container.startOpen(playerInventory.player);

        for (int slot = 0; slot < RunicForgeBlockEntity.RESULT_SLOT; slot++) {
            addSlot(new Slot(this.container, slot, CRAFT_SLOT_POSITIONS[slot][0], CRAFT_SLOT_POSITIONS[slot][1]));
        }
        addSlot(new Slot(this.container, RunicForgeBlockEntity.RESULT_SLOT, 81, 39) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(data);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 10 + col * 18, 93 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 10 + col * 18, 151));
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

        if (index < RunicForgeBlockEntity.CONTAINER_SIZE) {
            if (!moveItemStackTo(stack, RunicForgeBlockEntity.CONTAINER_SIZE, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, RunicForgeBlockEntity.RESULT_SLOT, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }

    public int getProcessProgress() {
        return data.get(0);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        container.stopOpen(player);
    }
}
