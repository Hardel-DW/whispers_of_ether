package fr.hardel.whispers_of_ether.client.mixin;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.WhispersOfEther;
import fr.hardel.whispers_of_ether.client.item.RuneTier;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RangeSelectItemModelProperties.class)
public class RangeSelectItemModelPropertiesMixin {

    @Shadow
    @Final
    public static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends RangeSelectItemModelProperty>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void registerCustomProperties(CallbackInfo ci) {
        ID_MAPPER.put(
            Identifier.fromNamespaceAndPath(WhispersOfEther.MOD_ID, "rune_tier"),
            RuneTier.MAP_CODEC);
    }
}
