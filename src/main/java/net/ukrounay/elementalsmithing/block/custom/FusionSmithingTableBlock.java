package net.ukrounay.elementalsmithing.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FusionSmithingTableBlock extends BlockWithEntity implements BlockEntityProvider {


    private static final VoxelShape SHAPE = VoxelShapes.union(
        Block.createCuboidShape(3, 0, 3, 13, 3, 13),
        Block.createCuboidShape(1, 0, 3, 15, 2, 13),
        Block.createCuboidShape(3, 0, 1, 13, 2, 15),
        Block.createCuboidShape(4, 3, 4, 12, 7, 12),
        Block.createCuboidShape(2, 7, 2, 14, 11, 14),
        Block.createCuboidShape(1, 8, 2, 15, 11, 14),
        Block.createCuboidShape(0, 9, 3, 16, 11, 13),
        Block.createCuboidShape(2, 8, 1, 14, 11, 15),
        Block.createCuboidShape(3, 9, 0, 13, 11, 16)
    );

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

//    public static final List<BlockPos> ENERGY_PROVIDER_OFFSETS = BlockPos.stream(-5, -5, -5, 5, 5, 5).filter(pos -> Math.abs(pos.getX()) != 1 && Math.abs(pos.getY()) != 1 && Math.abs(pos.getZ()) != 1).map(BlockPos::toImmutable).toList();

//    public static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
//        return world.getBlockState(tablePos.add(providerOffset)).isIn(ModTags.Blocks.CONDENSED_ENERGY_PROVIDER) && !world.getBlockState(tablePos.add(providerOffset.getX() / 2, providerOffset.getY(), providerOffset.getZ() / 2)).isIn(ModTags.Blocks.ENERGY_BARRIER);
//    }
//
//    public static int getPower(World world, BlockPos pos) {
//        int number = 0;
//        for (BlockPos blockPos : ENERGY_PROVIDER_OFFSETS)
//            if (!canAccessPowerProvider(world, pos, blockPos))
//                number++;
//        return number;
//    }

    public FusionSmithingTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

//    @Override
//    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
//        super.randomDisplayTick(state, world, pos, random);
//        int power = getPower(world, pos);
//        for (BlockPos blockPos : ENERGY_PROVIDER_OFFSETS) {
//            if (random.nextInt(3 + power) != 0 || !canAccessPowerProvider(world, pos, blockPos)) continue;
//            world.addParticle(ModParticles.ENERGY_PARTICLE, (double)pos.getX() + 0.5, (double)pos.getY() + 2.0, (double)pos.getZ() + 0.5, (double)((float)blockPos.getX() + random.nextFloat()) - 0.5, (float)blockPos.getY() - random.nextFloat() - 1.0f, (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5);
//        }
//    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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