package fr.hardel.whispers_of_ether.menu.runic_table;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record RunicTableHistoryEntry(
	ItemStack runeStack,
	RunicTableLogic.Outcome outcome,
	List<StatChange> statChanges) {
	public record StatChange(
		Identifier attributeId,
		double delta,
		AttributeModifier.Operation operation,
		boolean isPositiveEffect) {
		public static final StreamCodec<RegistryFriendlyByteBuf, StatChange> STREAM_CODEC = StreamCodec.composite(
			Identifier.STREAM_CODEC, StatChange::attributeId,
			ByteBufCodecs.DOUBLE, StatChange::delta,
			ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC), StatChange::operation,
			ByteBufCodecs.BOOL, StatChange::isPositiveEffect,
			StatChange::new);
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, RunicTableHistoryEntry> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public @NotNull RunicTableHistoryEntry decode(RegistryFriendlyByteBuf buf) {
			ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
			RunicTableLogic.Outcome outcome = buf.readEnum(RunicTableLogic.Outcome.class);
			int size = buf.readVarInt();
			List<StatChange> changes = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				changes.add(StatChange.STREAM_CODEC.decode(buf));
			}
			return new RunicTableHistoryEntry(stack, outcome, changes);
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, RunicTableHistoryEntry entry) {
			ItemStack.STREAM_CODEC.encode(buf, entry.runeStack);
			buf.writeEnum(entry.outcome);
			buf.writeVarInt(entry.statChanges.size());
			for (StatChange change : entry.statChanges) {
				StatChange.STREAM_CODEC.encode(buf, change);
			}
		}
	};
}
