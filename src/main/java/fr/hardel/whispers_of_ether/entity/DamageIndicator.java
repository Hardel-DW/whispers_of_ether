package fr.hardel.whispers_of_ether.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DamageIndicator extends Display.TextDisplay {

    private int lifeTime = 0;
    private Vec3 velocity;

    public DamageIndicator(EntityType<? extends Display.TextDisplay> entityType, Level level) {
        super(entityType, level);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("billboard", "center");
        nbt.putInt("background", 0);
        nbt.putFloat("shadow_radius", 0.0f);
        nbt.putInt("interpolation_duration", 1);
        nbt.putInt("start_interpolation", 0);
        this.readAdditionalSaveData(nbt);

        double spread = 0.15;
        this.velocity = new Vec3(
                (this.random.nextDouble() - 0.5) * spread,
                0.25 + this.random.nextDouble() * 0.1,
                (this.random.nextDouble() - 0.5) * spread);
    }

    public void setDamage(float damage) {
        String text = (damage % 1.0 == 0)
                ? String.valueOf((int) damage)
                : String.format("%.1f", damage);

        Component component = Component.literal(text).withStyle(style -> style.withColor(0xFF0000).withBold(true));
        CompoundTag nbt = new CompoundTag();
        nbt.putString("text", Component.Serializer.toJson(component, this.registryAccess()));
        this.readAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        super.tick();
        this.setPos(this.position().add(this.velocity));
        this.velocity = this.velocity.add(0, -0.025, 0).scale(0.9);

        if (++this.lifeTime > 30) {
            this.discard();
        }
    }
}
