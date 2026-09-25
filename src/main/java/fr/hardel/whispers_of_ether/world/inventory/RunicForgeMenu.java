package fr.hardel.whispers_of_ether.world.inventory;

import fr.hardel.whispers_of_ether.world.item.crafting.RunicForgeRecipe;
import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RunicForgeMenu extends RecipeBookMenu {
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

    @Override
    public RecipeBookMenu.@NotNull PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, RecipeHolder<?> recipe, ServerLevel level,
        Inventory inventory) {
        List<Slot> craftSlots = this.slots.subList(0, RunicForgeBlockEntity.RESULT_SLOT);
        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<RunicForgeRecipe>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
                RunicForgeMenu.this.fillCraftSlotsStackedContents(stackedContents);
            }

            @Override
            public void clearCraftingContent() {
                craftSlots.forEach(slot -> slot.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<RunicForgeRecipe> holder) {
                return holder.value().matches(CraftingInput.of(RunicForgeBlockEntity.RESULT_SLOT, 1, craftSlots.stream().map(Slot::getItem).toList()), level);
            }
        }, RunicForgeBlockEntity.RESULT_SLOT, 1, craftSlots, craftSlots, inventory, (RecipeHolder<RunicForgeRecipe>) recipe, useMaxItems, allowDroppingItemsToClear);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
        for (int slot = 0; slot < RunicForgeBlockEntity.RESULT_SLOT; slot++) {
            stackedContents.accountStack(this.container.getItem(slot));
        }
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.FURNACE;
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
