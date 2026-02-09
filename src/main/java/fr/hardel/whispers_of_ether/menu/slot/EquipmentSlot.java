package fr.hardel.whispers_of_ether.menu.slot;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import fr.hardel.whispers_of_ether.WhispersOfEther;

public class EquipmentSlot extends Slot {
    private static final TagKey<Item> RUNIC_TABLE_COMPATIBLE = TagKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_table_compatible"));

    public EquipmentSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.is(RUNIC_TABLE_COMPATIBLE);
    }
}
