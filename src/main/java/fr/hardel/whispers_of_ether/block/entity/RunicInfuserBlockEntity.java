package fr.hardel.whispers_of_ether.block.entity;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.component.ModItemComponent;
import fr.hardel.whispers_of_ether.component.item.RuneComponent;
import fr.hardel.whispers_of_ether.item.ModItems;
import fr.hardel.whispers_of_ether.menu.RunicInfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RunicInfuserBlockEntity extends BaseContainerBlockEntity {
    public static final TagKey<Item> RUNES_TAG = TagKey.create(Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runes"));
    public static final int XP_COST = 50;

    private static final Component TITLE = Component.translatable("container.whispers_of_ether.runic_infuser");
    private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public RunicInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNIC_INFUSER, pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return TITLE;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new RunicInfuserMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 1;
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
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
    }

    public boolean canInfuse(Player player) {
        ItemStack stack = getItem(0);
        return !stack.isEmpty() && stack.is(RUNES_TAG) && player.totalExperience >= XP_COST;
    }

    public void infuse(Player player) {
        if (!canInfuse(player) || level == null) {
            return;
        }

        ItemStack inputStack = getItem(0);
        int tier = getTierFromItem(inputStack.getItem());
        if (tier == 0) {
            return;
        }

        List<Map.Entry<Item, ResourceLocation>> entries = List.copyOf(ModItems.RUNE_TO_DATA.entrySet());
        Map.Entry<Item, ResourceLocation> randomEntry = entries.get(level.random.nextInt(entries.size()));

        player.giveExperiencePoints(-XP_COST);

        ItemStack newStack = new ItemStack(randomEntry.getKey());
        newStack.set(ModItemComponent.RUNES, new RuneComponent(randomEntry.getValue(), tier));
        setItem(0, newStack);
        setChanged();
    }

    private int getTierFromItem(Item item) {
        if (item == ModItems.RUNE) return 1;
        if (item == ModItems.REINFORCED_RUNE) return 2;
        if (item == ModItems.ECHO_RUNE) return 3;
        if (item == ModItems.NETHER_RUNE) return 4;
        if (item == ModItems.DRAGON_RUNE) return 5;
        return 0;
    }
}
