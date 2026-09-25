package fr.hardel.whispers_of_ether.world.level.block.entity;

import fr.hardel.whispers_of_ether.world.inventory.RunicForgeMenu;
import fr.hardel.whispers_of_ether.world.item.crafting.ModRecipes;
import fr.hardel.whispers_of_ether.world.item.crafting.RunicForgeInput;
import fr.hardel.whispers_of_ether.world.item.crafting.RunicForgeRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RunicForgeBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int RESULT_SLOT = 5;
    public static final int CONTAINER_SIZE = 6;
    public static final int PROCESS_TIME = 200;
    private static final int[] CRAFT_SLOTS = { 0, 1, 2, 3, 4 };
    private static final int[] RESULT_SLOTS = { RESULT_SLOT };
    private static final Component TITLE = Component.translatable("container.whispers_of_ether.runic_forge");
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int processProgress = 0;
    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return RunicForgeBlockEntity.this.processProgress;
        }

        @Override
        public void set(int index, int value) {
            RunicForgeBlockEntity.this.processProgress = value;
        }

        @Override
        public int getCount() {
            return 1;
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
    public int @NotNull [] getSlotsForFace(Direction direction) {
        return direction == Direction.DOWN ? RESULT_SLOTS : CRAFT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return slot != RESULT_SLOT;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot == RESULT_SLOT;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.processProgress = input.getIntOr("process_progress", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putInt("process_progress", this.processProgress);
    }

    public void tick(ServerLevel level) {
        RunicForgeInput input = RunicForgeInput.of(this.items.subList(0, RESULT_SLOT));
        Optional<RunicForgeRecipe> recipe = level.getServer().getRecipeManager().getRecipeFor(ModRecipes.RUNIC_FORGE_TYPE, input, level).map(RecipeHolder::value);
        ItemStack result = recipe.map(value -> value.assemble(input)).filter(this::fitsInResultSlot).orElse(ItemStack.EMPTY);
        if (result.isEmpty()) {
            this.processProgress = 0;
            updateLitState(false);
            return;
        }

        updateLitState(true);
        if (++this.processProgress >= PROCESS_TIME) {
            input.contents().canCraft(recipe.get(), this::consumeOne);
            addToResultSlot(result);
            this.processProgress = 0;
        }

        this.setChanged();
    }

    private boolean fitsInResultSlot(ItemStack result) {
        ItemStack output = this.items.get(RESULT_SLOT);
        return output.isEmpty() || ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void addToResultSlot(ItemStack result) {
        ItemStack output = this.items.get(RESULT_SLOT);
        if (output.isEmpty()) {
            this.items.set(RESULT_SLOT, result);
            return;
        }

        output.grow(result.getCount());
    }

    private void consumeOne(Holder<Item> item) {
        for (int slot : CRAFT_SLOTS) {
            ItemStack stack = this.items.get(slot);
            if (stack.is(item)) {
                stack.shrink(1);
                return;
            }
        }
    }

    private void updateLitState(boolean lit) {
        BlockState state = this.getBlockState();
        if (state.getValue(BlockStateProperties.LIT) != lit) {
            this.level.setBlock(this.worldPosition, state.setValue(BlockStateProperties.LIT, lit), 3);
        }
    }
}
