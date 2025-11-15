package fr.hardel.whispers_of_ether.spell.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hardel.whispers_of_ether.spell.timeline.TimelineScheduler;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

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
    public void execute(ServerLevel world, Entity caster, List<Entity> targets) {
        for (Entity target : targets) {
            Vec3 targetPos = target.position();
            BlockPos basePos = BlockPos.containing(targetPos.x, targetPos.y - 1, targetPos.z);

            while (basePos.getY() > world.getMinY() && world.getBlockState(basePos).isAir()) {
                basePos = basePos.below();
            }

            Vec3 groundPos = new Vec3(
                    Math.floor(targetPos.x) + 0.5,
                    basePos.getY() + 1,
                    Math.floor(targetPos.z) + 0.5);

            Display.BlockDisplay blockDisplay = new Display.BlockDisplay(
                    EntityType.BLOCK_DISPLAY, world);
            blockDisplay.setBlockState(Blocks.ICE.defaultBlockState());
            blockDisplay.setPos(groundPos.x, groundPos.y, groundPos.z);

            Vector3f initialScale = new Vector3f(1.0f, 0.1f, 1.0f);
            Vector3f finalScale = new Vector3f(1.0f, (float) height, 1.0f);
            Vector3f initialTranslation = new Vector3f(0.0f, -((float) height / 2.0f), 0.0f);
            Vector3f finalTranslation = new Vector3f(0.0f, 0.0f, 0.0f);

            blockDisplay.setTransformation(new com.mojang.math.Transformation(
                    initialTranslation,
                    new org.joml.Quaternionf(),
                    initialScale,
                    new org.joml.Quaternionf()));

            blockDisplay.setTransformationInterpolationDuration(20);
            blockDisplay.setTransformationInterpolationDelay(0);

            world.addFreshEntity(blockDisplay);

            blockDisplay.setTransformation(new com.mojang.math.Transformation(
                    finalTranslation,
                    new org.joml.Quaternionf(),
                    finalScale,
                    new org.joml.Quaternionf()));

            TimelineScheduler.scheduleDelayed(() -> {
                if (!blockDisplay.isRemoved()) {
                    Vector3f shrinkScale = new Vector3f(1.0f, 0.1f, 1.0f);
                    Vector3f shrinkTranslation = new Vector3f(0.0f, -((float) height / 2.0f), 0.0f);

                    blockDisplay.setTransformationInterpolationDuration(20);
                    blockDisplay.setTransformationInterpolationDelay(0);

                    blockDisplay.setTransformation(new com.mojang.math.Transformation(
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