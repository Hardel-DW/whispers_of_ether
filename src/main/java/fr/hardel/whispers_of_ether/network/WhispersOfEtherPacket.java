package fr.hardel.whispers_of_ether.network;

import fr.hardel.whispers_of_ether.WhispersOfEther;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class WhispersOfEtherPacket {
    public record SpellCast(Identifier spellId) implements CustomPayload {
        public static final CustomPayload.Id<SpellCast> ID = new CustomPayload.Id<>(
                Identifier.of(WhispersOfEther.MOD_ID, "spell_cast"));

        public static final PacketCodec<RegistryByteBuf, SpellCast> CODEC = PacketCodec.tuple(
                Identifier.PACKET_CODEC, SpellCast::spellId,
                SpellCast::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}