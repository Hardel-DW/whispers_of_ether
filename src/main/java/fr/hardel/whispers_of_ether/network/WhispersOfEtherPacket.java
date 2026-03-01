package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.menu.runic_table.RunicTableHistoryEntry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class WhispersOfEtherPacket {
    public record SpellCast(Identifier spellId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SpellCast> ID = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "spell_cast"));

        public static final StreamCodec<RegistryFriendlyByteBuf, SpellCast> CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, SpellCast::spellId,
            SpellCast::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record MultiJump() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<MultiJump> ID = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "multi_jump"));

        public static final StreamCodec<RegistryFriendlyByteBuf, MultiJump> CODEC = StreamCodec.of((RegistryFriendlyByteBuf buf, MultiJump val) -> {}, buf -> new MultiJump());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record RunicTableHistoryAdd(RunicTableHistoryEntry entry) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RunicTableHistoryAdd> ID = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_table_history_add"));

        public static final StreamCodec<RegistryFriendlyByteBuf, RunicTableHistoryAdd> CODEC = StreamCodec.composite(
            RunicTableHistoryEntry.STREAM_CODEC, RunicTableHistoryAdd::entry,
            RunicTableHistoryAdd::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record RunicTableHistoryClear() implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RunicTableHistoryClear> ID = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "runic_table_history_clear"));

        public static final StreamCodec<RegistryFriendlyByteBuf, RunicTableHistoryClear> CODEC = StreamCodec.of((RegistryFriendlyByteBuf buf, RunicTableHistoryClear val) -> {}, buf -> new RunicTableHistoryClear());

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}