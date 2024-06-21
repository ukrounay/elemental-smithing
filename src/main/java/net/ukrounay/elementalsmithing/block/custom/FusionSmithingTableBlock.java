package net.ukrounay.elementalsmithing.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class FusionSmithingTableBlock extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public FusionSmithingTableBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FusionSmithingTableBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof FusionSmithingTableBlockEntity) {
                ItemScatterer.spawn(world, pos, (FusionSmithingTableBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((FusionSmithingTableBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) player.openHandledScreen(screenHandlerFactory);
        }
        return ActionResult.SUCCESS;
    }
}