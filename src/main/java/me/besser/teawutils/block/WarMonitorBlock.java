//package me.besser.teawutils.block;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.EntityBlock;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//import org.jetbrains.annotations.NotNull;
//
//import javax.annotation.Nullable;
//
//public class WarMonitorBlock extends Block implements EntityBlock {
//    public WarMonitorBlock(Properties properties) {
//        super(properties);
//    }
//
//    @Override
//    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
//        if (!level.isClientSide) {
//            if (level.getBlockEntity(pos) instanceof WarMonitorBlockEntity be) {
//                be.sendStatus(player);
//            }
//        }
//        return InteractionResult.SUCCESS;
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return new WarMonitorBlockEntity(pos, state);
//    }
//}