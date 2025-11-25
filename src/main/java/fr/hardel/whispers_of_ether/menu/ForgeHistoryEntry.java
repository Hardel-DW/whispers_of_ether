package fr.hardel.whispers_of_ether.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ForgeHistoryEntry(
	ItemStack runeStack,
	RuneForgeLogic.Outcome outcome,
	List<StatChange> statChanges) {
	public record StatChange(
		ResourceLocation attributeId,
		double delta,
		AttributeModifier.Operation operation,
		boolean isPositiveEffect) {
		public static final StreamCodec<RegistryFriendlyByteBuf, StatChange> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, StatChange::attributeId,
			ByteBufCodecs.DOUBLE, StatChange::delta,
			ByteBufCodecs.fromCodec(AttributeModifier.Operation.CODEC), StatChange::operation,
			ByteBufCodecs.BOOL, StatChange::isPositiveEffect,
			StatChange::new);
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, ForgeHistoryEntry> STREAM_CODEC = new StreamCodec<>() {
		@Override
		public @NotNull ForgeHistoryEntry decode(RegistryFriendlyByteBuf buf) {
			ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
			RuneForgeLogic.Outcome outcome = buf.readEnum(RuneForgeLogic.Outcome.class);
			int size = buf.readVarInt();
			List<StatChange> changes = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				changes.add(StatChange.STREAM_CODEC.decode(buf));
			}
			return new ForgeHistoryEntry(stack, outcome, changes);
		}

		@Override
		public void encode(RegistryFriendlyByteBuf buf, ForgeHistoryEntry entry) {
			ItemStack.STREAM_CODEC.encode(buf, entry.runeStack);
			buf.writeEnum(entry.outcome);
			buf.writeVarInt(entry.statChanges.size());
			for (StatChange change : entry.statChanges) {
				StatChange.STREAM_CODEC.encode(buf, change);
			}
		}
	};
}
