package fr.hardel.whispers_of_ether.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RunicForgeMenu extends AbstractContainerMenu {
    private static final int INGREDIENT_SLOT_1 = 0;
    private static final int INGREDIENT_SLOT_2 = 1;
    private static final int INGREDIENT_SLOT_3 = 2;
    private static final int INGREDIENT_SLOT_4 = 3;
    private static final int INGREDIENT_SLOT_5 = 4;
    private static final int INPUT_SLOT = 5;
    private static final int CONTAINER_SIZE = 6;
    private static final int DATA_PROCESS_PROGRESS = 0;

    private final Container container;
    private final ContainerData data;

    public RunicForgeMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(CONTAINER_SIZE), new SimpleContainerData(1));
    }

    public RunicForgeMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.RUNIC_FORGE, containerId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        this.data = data;
        this.container.startOpen(playerInventory.player);
        this.addSlotListener(new ContainerChangeListener(() -> this.slotsChanged(this.container)));

        addSlot(new Slot(this.container, INGREDIENT_SLOT_1, 81, 12));
        addSlot(new Slot(this.container, INGREDIENT_SLOT_2, 52, 33));
        addSlot(new Slot(this.container, INGREDIENT_SLOT_3, 110, 33));
        addSlot(new Slot(this.container, INGREDIENT_SLOT_4, 62, 64));
        addSlot(new Slot(this.container, INGREDIENT_SLOT_5, 100, 64));
        addSlot(new Slot(this.container, INPUT_SLOT, 81, 39) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(data);
    }

    private record ContainerChangeListener(Runnable changeCallback) implements ContainerListener {
        @Override
        public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
            changeCallback.run();
        }

        @Override
        public void dataChanged(AbstractContainerMenu container, int id, int value) {}
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

        if (index < CONTAINER_SIZE) {
            if (!moveItemStackTo(stack, CONTAINER_SIZE, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, CONTAINER_SIZE, false)) {
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
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
    }

    public int getProcessProgress() {
        return data.get(DATA_PROCESS_PROGRESS);
    }

    public int getMaxProcessTime() {
        return 200;
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
