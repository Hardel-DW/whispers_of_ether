package fr.hardel.whispers_of_ether.block;

import com.mojang.serialization.MapCodec;
import fr.hardel.whispers_of_ether.block.entity.RunicInfuserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class RunicInfuserBlock extends BaseEntityBlock {
    public static final MapCodec<RunicInfuserBlock> CODEC = simpleCodec(RunicInfuserBlock::new);

    public RunicInfuserBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunicInfuserBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof RunicInfuserBlockEntity infuser) {
            player.openMenu(infuser);
        }

        return InteractionResult.CONSUME;
    }
}
