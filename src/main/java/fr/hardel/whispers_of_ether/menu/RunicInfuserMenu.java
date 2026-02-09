package fr.hardel.whispers_of_ether.menu;

import fr.hardel.whispers_of_ether.block.entity.RunicInfuserBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunicInfuserMenu extends AbstractContainerMenu {
    private static final TagKey<Item> RUNES_TAG = TagKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runes"));
    private static final int SLOT_X = 82;
    private static final int SLOT_Y = 38;

    private final Container container;
    private final @Nullable RunicInfuserBlockEntity blockEntity;
    private final Player player;

    public RunicInfuserMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(1));
    }

    public RunicInfuserMenu(int containerId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.RUNIC_INFUSER, containerId);
        this.container = container;
        this.blockEntity = container instanceof RunicInfuserBlockEntity be ? be : null;
        this.player = playerInventory.player;
        container.startOpen(player);
        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int slotIndex, ItemStack stack) {
                slotsChanged(container);
            }

            @Override
            public void dataChanged(AbstractContainerMenu menu, int id, int value) {}
        });

        addSlot(new Slot(container, 0, SLOT_X, SLOT_Y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(RUNES_TAG);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
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

        if (index == 0) {
            if (!moveItemStackTo(stack, 1, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.is(RUNES_TAG)) {
            if (!moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        } else {
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
        if (blockEntity == null) {
            clearContainer(player, container);
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (blockEntity != null && blockEntity.canInfuse(player)) {
            blockEntity.infuse(player);
        }
    }
}
