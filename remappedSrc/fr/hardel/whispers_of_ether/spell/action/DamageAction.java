package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;

public record DamageAction(float amount, RegistryEntry<DamageType> damageType) implements Action {

    public static final MapCodec<DamageAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("amount").forGetter(DamageAction::amount),
            DamageType.ENTRY_CODEC.fieldOf("damage_type").forGetter(DamageAction::damageType))
            .apply(instance, DamageAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.DAMAGE;
    }

    @Override
    public void execute(ServerWorld world, Entity caster, List<Entity> targets) {
        DamageSource damageSource = new DamageSource(damageType, caster);

        for (Entity target : targets) {
            if (target instanceof LivingEntity livingTarget) {
                if (amount > 0) {
                    livingTarget.damage(world, damageSource, amount);
                } else {
                    livingTarget.heal(-amount);
                }
            }
        }
    }
}