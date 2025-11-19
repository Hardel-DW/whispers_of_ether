package fr.hardel.whispers_of_ether.entity;

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
        this.setBillboardConstraints(BillboardConstraints.CENTER);
        this.setBackgroundColor(0);
        this.setShadowRadius(0.0f);
        this.setTransformationInterpolationDuration(1);
        this.setTransformationInterpolationDelay(0);

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

        this.setText(Component.literal(text).withStyle(style -> style.withColor(0xFF0000).withBold(true)));
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
