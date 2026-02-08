package fr.hardel.whispers_of_ether.block.entity;

import fr.hardel.whispers_of_ether.menu.RunicForgeMenu;
import fr.hardel.whispers_of_ether.recipe.ModRecipes;
import fr.hardel.whispers_of_ether.recipe.RunicForgeRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RunicForgeBlockEntity extends BaseContainerBlockEntity {
    private static final int CONTAINER_SIZE = 6;
    private static final int PROCESS_TIME = 200;
    private static final int DATA_PROCESS_PROGRESS = 0;
    private static final int NUM_DATA_VALUES = 1;
    private static final Component TITLE = Component.translatable("container.whispers_of_ether.runic_forge");
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int processProgress = 0;
    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return index == DATA_PROCESS_PROGRESS ? RunicForgeBlockEntity.this.processProgress : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == DATA_PROCESS_PROGRESS) {
                RunicForgeBlockEntity.this.processProgress = value;
            }
        }

        @Override
        public int getCount() {
            return NUM_DATA_VALUES;
        }
    };

    public RunicForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNIC_FORGE, pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return TITLE;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new RunicForgeMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.processProgress = tag.getInt("process_progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("process_progress", this.processProgress);
    }

    public static void tick(Level level, RunicForgeBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        blockEntity.updateProgress();
    }

    private void updateProgress() {
        RunicForgeRecipeInput recipeInput = new RunicForgeRecipeInput(this.items.subList(0, 6).toArray(ItemStack[]::new));
        if (this.level == null || this.level.getServer() == null) {
            return;
        }

        Optional<RecipeHolder<RunicForgeRecipe>> recipeHolder = this.level.getServer().getRecipeManager().getRecipeFor(ModRecipes.RUNIC_FORGE_TYPE, recipeInput, this.level);
        if (recipeHolder.isEmpty()) {
            this.processProgress = 0;
            updateLitState(false);
            return;
        }

        if (!canOutputResult(recipeHolder.get().value())) {
            this.processProgress = 0;
            updateLitState(false);
            return;
        }

        if (++this.processProgress < PROCESS_TIME) {
            updateLitState(true);
            this.setChanged();
            return;
        }

        updateLitState(false);

        ItemStack result = recipeHolder.get().value().assemble(recipeInput, this.level.registryAccess());
        for (int i = 0; i < 5; i++) {
            this.removeItem(i, 1);
        }

        ItemStack remaining = pushToAdjacentContainers(result);
        if (remaining.isEmpty()) {
            this.removeItem(5, 1);
        } else {
            ItemStack inputItem = this.getItem(5);
            if (!inputItem.isEmpty() && inputItem.is(remaining.getItem())) {
                inputItem.grow(remaining.getCount());
            } else {
                this.setItem(5, remaining);
            }
        }

        this.processProgress = 0;
        this.setChanged();
    }

    private boolean canOutputResult(RunicForgeRecipe recipe) {
        ItemStack result = recipe.result();
        ItemStack inputItem = this.getItem(5);

        if (inputItem.isEmpty() && result.getItem().getDefaultMaxStackSize() < result.getCount()) {
            return false;
        }

        if (!inputItem.isEmpty() && inputItem.is(result.getItem())) {
            return inputItem.getCount() + result.getCount() <= inputItem.getMaxStackSize();
        }

        return true;
    }

    private void updateLitState(boolean lit) {
        if (this.level != null && this.level.getBlockState(this.worldPosition).getValue(BlockStateProperties.LIT) != lit) {
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(BlockStateProperties.LIT, lit), 3);
        }
    }

    private ItemStack pushToAdjacentContainers(ItemStack result) {
        ItemStack remaining = result;
        for (Direction direction : new Direction[] { Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST }) {
            if (remaining.isEmpty()) {
                break;
            }
            BlockPos adjacentPos = this.worldPosition.relative(direction);
            Container adjacent = HopperBlockEntity.getContainerAt(this.level, adjacentPos);

            if (adjacent != null) {
                remaining = HopperBlockEntity.addItem(this, adjacent, remaining, direction);
            }
        }
        return remaining;
    }

    private record RunicForgeRecipeInput(ItemStack[] items) implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(int slot) {
            return slot >= 0 && slot < items.length ? items[slot] : ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return items.length;
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack item : items) {
                if (!item.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }
}
