
package fr.hardel.whispers_of_ether.world.level.block;

import fr.hardel.whispers_of_ether.world.level.block.entity.ModBlockEntities;
import fr.hardel.whispers_of_ether.world.level.block.entity.RunicForgeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class RunicForgeBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final Vec3 CHIMNEY_TOP = new Vec3(11.5 / 16.0, 19.0 / 16.0, 11.5 / 16.0);

    public RunicForgeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.LIT);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunicForgeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel ? createTickerHelper(type, ModBlockEntities.RUNIC_FORGE, (lvl, pos, st, forge) -> forge.tick(serverLevel)) : null;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player,
        final BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RunicForgeBlockEntity runicForge) {
            player.openMenu(runicForge);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        if (!state.getValue(BlockStateProperties.LIT)) {
            return;
        }

        Vec3 chimney = rotateFromNorth(CHIMNEY_TOP, state.getValue(FACING));
        double x = pos.getX() + chimney.x + (random.nextDouble() - 0.5) * 0.2;
        double z = pos.getZ() + chimney.z + (random.nextDouble() - 0.5) * 0.2;
        level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, x, pos.getY() + chimney.y, z, 0.0, 0.07, 0.0);
    }

    // The model faces north, the blockstate turns it clockwise around the block center.
    private static Vec3 rotateFromNorth(Vec3 point, Direction facing) {
        Vec3 rotated = point.subtract(0.5, 0.0, 0.5);
        for (int step = 0; step < (facing.get2DDataValue() + 2) % 4; step++) {
            rotated = new Vec3(-rotated.z, rotated.y, rotated.x);
        }
        return rotated.add(0.5, 0.0, 0.5);
    }
}
