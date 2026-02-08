package fr.hardel.whispers_of_ether.item;

import fr.hardel.whispers_of_ether.entity.ModEntities;
import fr.hardel.whispers_of_ether.entity.TargetDummy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class TargetDummyItem extends Item {
    public TargetDummyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        }

        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos2 = blockPos.relative(direction);

        TargetDummy dummy = ModEntities.TARGET_DUMMY.create(serverLevel, EntitySpawnReason.SPAWN_ITEM_USE);
        if (dummy == null) {
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }

        dummy.moveTo(blockPos2.getX() + 0.5, blockPos2.getY(), blockPos2.getZ() + 0.5,
                context.getHorizontalDirection().toYRot() + 180.0F, 0.0F);
        serverLevel.addFreshEntity(dummy);
        level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos2);
        context.getItemInHand().shrink(1);

        return InteractionResult.SUCCESS;
    }
}
