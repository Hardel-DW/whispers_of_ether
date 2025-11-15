package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class WhispersOfEtherPacket {
    public record SpellCast(ResourceLocation spellId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SpellCast> ID = new CustomPacketPayload.Type<>(
                ResourceLocation.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "spell_cast"));

        public static final StreamCodec<RegistryFriendlyByteBuf, SpellCast> CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, SpellCast::spellId,
                SpellCast::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}