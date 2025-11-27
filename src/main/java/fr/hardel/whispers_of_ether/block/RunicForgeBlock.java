
package fr.hardel.whispers_of_ether.block;

import com.mojang.serialization.MapCodec;

import fr.hardel.whispers_of_ether.block.entity.ModBlockEntities;
import fr.hardel.whispers_of_ether.block.entity.RunicForgeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunicForgeBlock extends BaseEntityBlock {
    public static final MapCodec<RunicForgeBlock> CODEC = simpleCodec(RunicForgeBlock::new);

    public RunicForgeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunicForgeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type != ModBlockEntities.RUNIC_FORGE) {
            return null;
        }
        return level.isClientSide() ? null : (lvl, pos, st, entity) -> RunicForgeBlockEntity.tick(lvl, (RunicForgeBlockEntity) entity);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
        BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RunicForgeBlockEntity runicForge) {
            player.openMenu(runicForge);
        }

        return InteractionResult.CONSUME;
    }
}
