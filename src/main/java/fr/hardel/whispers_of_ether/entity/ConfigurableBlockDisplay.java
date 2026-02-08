package fr.hardel.whispers_of_ether.entity;

import com.mojang.math.Transformation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurableBlockDisplay extends Display.BlockDisplay {

    public ConfigurableBlockDisplay(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void configure(BlockState blockState, Transformation transformation, int interpolationDuration, int interpolationDelay) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("block_state", NbtUtils.writeBlockState(blockState));
        Transformation.EXTENDED_CODEC.encodeStart(NbtOps.INSTANCE, transformation)
                .ifSuccess(tag -> nbt.put("transformation", tag));
        nbt.putInt("interpolation_duration", interpolationDuration);
        nbt.putInt("start_interpolation", interpolationDelay);
        this.readAdditionalSaveData(nbt);
    }

    public void updateTransformation(Transformation transformation) {
        CompoundTag nbt = new CompoundTag();
        Transformation.EXTENDED_CODEC.encodeStart(NbtOps.INSTANCE, transformation)
                .ifSuccess(tag -> nbt.put("transformation", tag));
        this.readAdditionalSaveData(nbt);
    }
}
