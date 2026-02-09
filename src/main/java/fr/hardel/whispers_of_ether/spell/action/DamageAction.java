package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public record DamageAction(float amount, Holder<DamageType> damageType) implements Action {

    public static final MapCodec<DamageAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("amount").forGetter(DamageAction::amount),
            DamageType.CODEC.fieldOf("damage_type").forGetter(DamageAction::damageType))
            .apply(instance, DamageAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.DAMAGE;
    }

    @Override
    public void execute(ServerLevel world, Entity caster, List<Entity> targets) {
        DamageSource damageSource = new DamageSource(damageType, caster);

        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget) {
                if (amount > 0) {
                    livingTarget.hurt(damageSource, amount);
                } else {
                    livingTarget.heal(-amount);
                }
            }
        }
    }
}