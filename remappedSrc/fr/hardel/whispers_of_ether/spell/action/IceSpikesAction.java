package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.timeline.TimelineScheduler;
import org.joml.Vector3f;

import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public record IceSpikesAction(int height, int duration) implements Action {

    public static final MapCodec<IceSpikesAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.optionalFieldOf("height", 3).forGetter(IceSpikesAction::height),
            Codec.INT.optionalFieldOf("duration", 100).forGetter(IceSpikesAction::duration))
            .apply(instance, IceSpikesAction::new));

    @Override
    public ActionType<?> getType() {
        return ActionType.ICE_SPIKES;
    }

    @Override
    public void execute(ServerWorld world, Entity caster, List<Entity> targets) {
        for (Entity target : targets) {
            Vec3d targetPos = target.getEntityPos();
            BlockPos basePos = BlockPos.ofFloored(targetPos.x, targetPos.y - 1, targetPos.z);

            while (basePos.getY() > world.getBottomY() && world.getBlockState(basePos).isAir()) {
                basePos = basePos.down();
            }

            Vec3d groundPos = new Vec3d(
                    Math.floor(targetPos.x) + 0.5,
                    basePos.getY() + 1,
                    Math.floor(targetPos.z) + 0.5);

            DisplayEntity.BlockDisplayEntity blockDisplay = new DisplayEntity.BlockDisplayEntity(
                    EntityType.BLOCK_DISPLAY, world);
            blockDisplay.setBlockState(Blocks.ICE.getDefaultState());
            blockDisplay.setPosition(groundPos.x, groundPos.y, groundPos.z);

            Vector3f initialScale = new Vector3f(1.0f, 0.1f, 1.0f);
            Vector3f finalScale = new Vector3f(1.0f, (float) height, 1.0f);
            Vector3f initialTranslation = new Vector3f(0.0f, -((float) height / 2.0f), 0.0f);
            Vector3f finalTranslation = new Vector3f(0.0f, 0.0f, 0.0f);

            blockDisplay.setTransformation(new net.minecraft.util.math.AffineTransformation(
                    initialTranslation,
                    new org.joml.Quaternionf(),
                    initialScale,
                    new org.joml.Quaternionf()));

            blockDisplay.setInterpolationDuration(20);
            blockDisplay.setStartInterpolation(0);

            world.spawnEntity(blockDisplay);

            blockDisplay.setTransformation(new net.minecraft.util.math.AffineTransformation(
                    finalTranslation,
                    new org.joml.Quaternionf(),
                    finalScale,
                    new org.joml.Quaternionf()));

            TimelineScheduler.scheduleDelayed(() -> {
                if (!blockDisplay.isRemoved()) {
                    Vector3f shrinkScale = new Vector3f(1.0f, 0.1f, 1.0f);
                    Vector3f shrinkTranslation = new Vector3f(0.0f, -((float) height / 2.0f), 0.0f);

                    blockDisplay.setInterpolationDuration(20);
                    blockDisplay.setStartInterpolation(0);

                    blockDisplay.setTransformation(new net.minecraft.util.math.AffineTransformation(
                            shrinkTranslation,
                            new org.joml.Quaternionf(),
                            shrinkScale,
                            new org.joml.Quaternionf()));

                    TimelineScheduler.scheduleDelayed(() -> {
                        if (!blockDisplay.isRemoved()) {
                            blockDisplay.discard();
                        }
                    }, 20);
                }
            }, duration);
        }
    }
}