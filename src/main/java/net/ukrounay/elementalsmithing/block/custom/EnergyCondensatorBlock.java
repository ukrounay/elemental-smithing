package net.ukrounay.elementalsmithing.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import net.ukrounay.elementalsmithing.block.entity.ModBlockEntities;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.util.ModTags;
import net.ukrounay.elementalsmithing.util.RotationHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class EnergyCondensatorBlock extends BlockWithEntity implements BlockEntityProvider, Waterloggable {

    private static final VoxelShape UP_SHAPE;
    private static final VoxelShape DOWN_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape WEST_SHAPE;

    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_UP;
    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_DOWN;
    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_NORTH;
    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_SOUTH;
    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_EAST;
    private static final List<BlockPos> ENERGY_PROVIDER_OFFSETS_WEST;


    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;

    public EnergyCondensatorBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState())
                .with(FACING, Direction.UP))
                .with(WATERLOGGED, false));

    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return (BlockState)this.getDefaultState().with(FACING, ctx.getSide()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, WATERLOGGED});
    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }


    public static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
        return world.getBlockState(tablePos.add(providerOffset)).isIn(ModTags.Blocks.ENERGY_PROVIDER);
    }


    public static List<BlockPos> getEnergyProviderOffsets(Direction facing) {
        return switch (facing) {
            case UP -> ENERGY_PROVIDER_OFFSETS_UP;
            case DOWN -> ENERGY_PROVIDER_OFFSETS_DOWN;
            case NORTH -> ENERGY_PROVIDER_OFFSETS_NORTH;
            case SOUTH -> ENERGY_PROVIDER_OFFSETS_SOUTH;
            case EAST -> ENERGY_PROVIDER_OFFSETS_EAST;
            case WEST -> ENERGY_PROVIDER_OFFSETS_WEST;
        };
    }

    public static float getPower(World world, BlockPos pos) {
        List<BlockPos> offsets = getEnergyProviderOffsets(world.getBlockState(pos).get(EnergyCondensatorBlock.FACING));

        int number = 0;
        for (BlockPos blockPos : offsets)
            if (!canAccessPowerProvider(world, pos, blockPos))
                number++;

        return (float) number / offsets.size();
    }

    public static float getPower(World world, BlockPos pos, BlockState blockstate) {
        List<BlockPos> offsets = getEnergyProviderOffsets(blockstate.get(EnergyCondensatorBlock.FACING));

        int number = 0;
        for (BlockPos blockPos : offsets)
            if (!canAccessPowerProvider(world, pos, blockPos))
                number++;

        return (float) number / offsets.size();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        for (BlockPos blockPos : getEnergyProviderOffsets(state.get(FACING))) {
            if (random.nextInt(8) != 0 || !canAccessPowerProvider(world, pos, blockPos)) continue;
            world.addParticle(ModParticles.ENERGY_PARTICLE,
                    pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5,
                    (double)((float)blockPos.getX() + random.nextFloat()) - 0.5,
                    (float)blockPos.getY() - random.nextFloat() - 1.0f,
                    (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5
            );
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCondensatorBlockEntity ecbe) {
            if (ecbe.isStorageOwner()) {
                Vec3i offset = state.get(FACING).getVector();
                if (random.nextInt(4) == 0 && ecbe.isCharging())
                    world.addParticle(ModParticles.ENERGY_FLUCTUATION_PARTICLE,
                            pos.getX() + offset.getX() + 0.5f,
                            pos.getY() + offset.getY() + 0.5f,
                            pos.getZ() + offset.getZ() + 0.5f,
                            0,     0, 0
                );

            }

            for (EnergyCondensatorBlockEntity partner : ecbe.getArrayPartners() ) {
                BlockPos p_pos = partner.getPos();
                float chance = random.nextFloat();
                if (chance < 0.35) {
                    world.addParticle(ModParticles.ENERGY_DISCHARGE_PARTICLE,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            p_pos.getX() + 0.5, p_pos.getY() + 0.5, p_pos.getZ() + 0.5
                    );
                } else if (chance < 0.7) {
                    world.addParticle(ModParticles.ENERGY_DISCHARGE_PARTICLE,
                            p_pos.getX() + 0.5, p_pos.getY() + 0.5, p_pos.getZ() + 0.5,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5
                    );
                }
            }

        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyCondensatorBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {return;}
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCondensatorBlockEntity be) {
            ItemScatterer.spawn(world, pos, be.getItems());
            be.clear();
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof EnergyCondensatorBlockEntity ecbe) {
            if (!world.isClient() && ecbe.interact(player, hand)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

//    @Override
//    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
//        super.onPlaced(world, pos, state, placer, itemStack);
//        if (world.getBlockEntity(pos) instanceof EnergyCondensatorBlockEntity be) {
//            be.refreshArray();
//        }
//    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient())
            return EnergyCondensatorBlock.checkType(type, ModBlockEntities.ENERGY_CONDENSATOR, EnergyCondensatorBlockEntity::tick);
        return EnergyCondensatorBlock.checkType(type, ModBlockEntities.ENERGY_CONDENSATOR, EnergyCondensatorBlockEntity::tickClient);
    }



    static {
        FACING = Properties.FACING;
        WATERLOGGED = Properties.WATERLOGGED;

        UP_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(4, 0, 4,12, 1, 12),
                Block.createCuboidShape(11, 1, 5,12, 3, 11),
                Block.createCuboidShape(4, 1, 5, 5, 3, 11),
                Block.createCuboidShape(5, 1, 11, 11, 3, 12),
                Block.createCuboidShape(5, 1, 4, 11, 3, 5),
                Block.createCuboidShape(5, 3, 5, 11, 4, 11),
                Block.createCuboidShape(5.5, 4, 5.5, 10.5, 6.5, 10.5),
                Block.createCuboidShape(4.5, 6.5, 4.5, 11.5, 9.5, 11.5),
                Block.createCuboidShape(6, 11, 6, 7, 12.5, 7),
                Block.createCuboidShape(4.5, 10, 4.5, 6.5, 12, 6.5),
                Block.createCuboidShape(4, 7.5, 4, 6, 11, 6),
                Block.createCuboidShape(6, 11, 9, 7, 12.5, 10),
                Block.createCuboidShape(4.5, 10, 9.5, 6.5, 12, 11.5),
                Block.createCuboidShape(4, 7.5, 10, 6, 11, 12),
                Block.createCuboidShape(9, 11, 9, 10, 12.5, 10),
                Block.createCuboidShape(9.5, 10, 9.5, 11.5, 12, 11.5),
                Block.createCuboidShape(10, 7.5, 10, 12, 11, 12),
                Block.createCuboidShape(10, 7.5, 4, 12, 11, 6),
                Block.createCuboidShape(9, 11, 6, 10, 12.5, 7),
                Block.createCuboidShape(9.5, 10, 4.5 , 11.5, 12, 6.5)
        );
        DOWN_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(4, 15, 4, 12, 16, 12),
                Block.createCuboidShape(11, 13, 5, 12, 15, 11),
                Block.createCuboidShape(4, 13, 5, 5, 15, 11),
                Block.createCuboidShape(5, 13, 4, 11, 15, 5),
                Block.createCuboidShape(5, 13, 11, 11, 15, 12),
                Block.createCuboidShape(5, 12, 5, 11, 13, 11),
                Block.createCuboidShape(5.5, 9.5, 5.5, 10.5, 12, 10.5),
                Block.createCuboidShape(4.5, 6.5, 4.5, 11.5, 9.5, 11.5),
                Block.createCuboidShape(6, 3.5, 9, 7, 5, 10),
                Block.createCuboidShape(4.5, 4, 9.5, 6.5, 6, 11.5),
                Block.createCuboidShape(4, 5, 10, 6, 8.5, 12),
                Block.createCuboidShape(6, 3.5, 6, 7, 5, 7),
                Block.createCuboidShape(4.5, 4, 4.5, 6.5, 6, 6.5),
                Block.createCuboidShape(4, 5, 4, 6, 8.5, 6),
                Block.createCuboidShape(9, 3.5, 6, 10, 5, 7),
                Block.createCuboidShape(9.5, 4, 4.5, 11.5, 6, 6.5),
                Block.createCuboidShape(10, 5, 4, 12, 8.5, 6),
                Block.createCuboidShape(10, 5, 10, 12, 8.5, 12),
                Block.createCuboidShape(9, 3.5, 9, 10, 5, 10),
                Block.createCuboidShape(9.5, 4, 9.5, 11.5, 6, 11.5)
        );
        NORTH_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(4, 4, 15, 12, 12, 16),
                Block.createCuboidShape(11, 5, 13, 12, 11, 15),
                Block.createCuboidShape(4, 5, 13, 5, 11, 15),
                Block.createCuboidShape(5, 11, 13, 11, 12, 15),
                Block.createCuboidShape(5, 4, 13, 11, 5, 15),
                Block.createCuboidShape(5, 5, 12, 11, 11, 13),
                Block.createCuboidShape(5.5, 5.5, 9.5, 10.5, 10.5, 12),
                Block.createCuboidShape(4.5, 4.5, 6.5, 11.5, 11.5, 9.5),

                Block.createCuboidShape(6, 6, 3.5, 7, 7, 5),
                Block.createCuboidShape(4.5, 4.5, 4, 6.5, 6.5, 6),
                Block.createCuboidShape(4, 4, 5, 6, 6, 8.5),

                Block.createCuboidShape(6, 9, 3.5, 7, 10, 5),
                Block.createCuboidShape(4.5, 9.5, 4, 6.5, 11.5, 6),
                Block.createCuboidShape(4, 10, 5, 6, 12, 8.5),

                Block.createCuboidShape(9, 9, 3.5, 10, 10, 5),
                Block.createCuboidShape(9.5, 9.5, 4, 11.5, 11.5, 6),
                Block.createCuboidShape(10, 10, 5, 12, 12, 8.5),

                Block.createCuboidShape(10, 4, 5, 12, 6, 8.5),
                Block.createCuboidShape(9, 6, 3.5, 10, 7, 5),
                Block.createCuboidShape(9.5, 4.5, 4, 11.5, 6.5, 6)
        );
        SOUTH_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(4, 4, 0, 12, 12, 1),
                Block.createCuboidShape(11, 5, 1, 12, 11, 3),
                Block.createCuboidShape(4, 5, 1, 5, 11, 3),
                Block.createCuboidShape(5, 4, 1, 11, 5, 3),
                Block.createCuboidShape(5, 11, 1, 11, 12, 3),
                Block.createCuboidShape(5, 5, 3, 11, 11, 4),
                Block.createCuboidShape(5.5, 5.5, 4, 10.5, 10.5, 6.5),
                Block.createCuboidShape(4.5, 4.5, 6.5, 11.5, 11.5, 9.5),

                Block.createCuboidShape(6, 9, 11, 7, 10, 12.5),
                Block.createCuboidShape(4.5, 9.5, 10, 6.5, 11.5, 12),
                Block.createCuboidShape(4, 10, 7.5, 6, 12, 11),

                Block.createCuboidShape(6, 6, 11, 7, 7, 12.5),
                Block.createCuboidShape(4.5, 4.5, 10, 6.5, 6.5, 12),
                Block.createCuboidShape(4, 4, 7.5, 6, 6, 11),

                Block.createCuboidShape(9, 6, 11, 10, 7, 12.5),
                Block.createCuboidShape(9.5, 4.5, 10, 11.5, 6.5, 12),
                Block.createCuboidShape(10, 4, 7.5, 12, 6, 11),

                Block.createCuboidShape(10, 10, 7.5, 12, 12, 11),
                Block.createCuboidShape(9, 9, 11, 10, 10, 12.5),
                Block.createCuboidShape(9.5, 9.5, 10, 11.5, 11.5, 12)
        );
        EAST_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(0, 4, 4, 1, 12, 12),
                Block.createCuboidShape(1, 5, 5, 3, 12, 11),
                Block.createCuboidShape(1, 4, 5, 3, 5, 11),
                Block.createCuboidShape(1, 5, 11, 3, 11, 12),
                Block.createCuboidShape(1, 5, 4, 3, 11, 5),
                Block.createCuboidShape(3, 5, 5, 4, 11, 11),
                Block.createCuboidShape(4, 5.5, 5.5, 6.5, 10.5, 10.5),
                Block.createCuboidShape(6.5, 4.5, 4.5, 9.5, 11.5, 11.5),
                Block.createCuboidShape(11, 6, 6, 12.5, 7, 7),
                Block.createCuboidShape(10, 4.5, 4.5, 12, 6.5, 6.5),
                Block.createCuboidShape(7.5, 4, 4, 11, 6, 6),
                Block.createCuboidShape(11, 6, 9, 12.5, 7, 10),
                Block.createCuboidShape(10, 4.5, 9.5, 12, 6.5, 11.5),
                Block.createCuboidShape(7.5, 4, 10, 11, 6, 12),
                Block.createCuboidShape(11, 9, 9, 12.5, 10, 10),
                Block.createCuboidShape(10, 9.5, 9.5, 12, 11.5, 11.5),
                Block.createCuboidShape(7.5, 10, 10, 11, 12, 12),
                Block.createCuboidShape(7.5, 10, 4, 11, 12, 6),
                Block.createCuboidShape(11, 9, 6, 12.5, 10, 7),
                Block.createCuboidShape(10, 9.5, 4.5, 12, 11.5, 6.5)
        );
        WEST_SHAPE = VoxelShapes.union(
                Block.createCuboidShape(15, 4, 4, 16, 12, 12),
                Block.createCuboidShape(13, 11, 5, 15, 12, 11),
                Block.createCuboidShape(13, 4, 5, 15, 5, 11),
                Block.createCuboidShape(13, 5, 11, 15, 11, 12),
                Block.createCuboidShape(13, 5, 4, 15, 11, 5),
                Block.createCuboidShape(12, 5, 5, 13, 11, 11),
                Block.createCuboidShape(9.5, 5.5, 5.5, 12, 10.5, 10.5),
                Block.createCuboidShape(6.5, 4.5, 4.5, 9.5, 11.5, 11.5),
                Block.createCuboidShape(3.5, 6, 6, 5, 7, 7),
                Block.createCuboidShape(4, 4.5, 4.5, 6, 6.5, 6.5),
                Block.createCuboidShape(5, 4, 4, 8.5, 6, 6),
                Block.createCuboidShape(3.5, 6, 9, 5, 7, 10),
                Block.createCuboidShape(4, 4.5, 9.5, 6, 6.5, 11.5),
                Block.createCuboidShape(5, 4, 10, 8.5, 6, 12),
                Block.createCuboidShape(3.5, 9, 9, 5, 10, 10),
                Block.createCuboidShape(4, 9.5, 9.5, 6, 11.5, 11.5),
                Block.createCuboidShape(5, 10, 10, 8.5, 12, 12),
                Block.createCuboidShape(5, 10, 4, 8.5, 12, 6),
                Block.createCuboidShape(3.5, 9, 6, 5, 10, 7),
                Block.createCuboidShape(4, 9.5, 4.5, 6, 11.5, 6.5)
        );

        ENERGY_PROVIDER_OFFSETS_UP = BlockPos
                .stream(-2, -2, -2, 2, 0, 2)
                .filter(pos -> (pos.getX() == 0 || pos.getZ() == 0)
                        && Math.abs(pos.getX()) + Math.abs(pos.getY()) <= 2
                        && Math.abs(pos.getZ()) + Math.abs(pos.getY()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

        ENERGY_PROVIDER_OFFSETS_DOWN = BlockPos
                .stream(-2, 0, -2, 2, 2, 2)
                .filter(pos -> (pos.getX() == 0 || pos.getZ() == 0)
                        && Math.abs(pos.getX()) + Math.abs(pos.getY()) <= 2
                        && Math.abs(pos.getZ()) + Math.abs(pos.getY()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

        ENERGY_PROVIDER_OFFSETS_NORTH = BlockPos
                .stream(-2, -2, 0, 2, 2, 2)
                .filter(pos -> (pos.getX() == 0 || pos.getY() == 0)
                        && Math.abs(pos.getX()) + Math.abs(pos.getZ()) <= 2
                        && Math.abs(pos.getY()) + Math.abs(pos.getZ()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

        ENERGY_PROVIDER_OFFSETS_SOUTH = BlockPos
                .stream(-2, -2, -2, 2, 2, 0)
                .filter(pos -> (pos.getX() == 0 || pos.getY() == 0)
                        && Math.abs(pos.getX()) + Math.abs(pos.getZ()) <= 2
                        && Math.abs(pos.getY()) + Math.abs(pos.getZ()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

        ENERGY_PROVIDER_OFFSETS_EAST = BlockPos
                .stream(-2, -2, -2, 0, 2, 2)
                .filter(pos -> (pos.getY() == 0 || pos.getZ() == 0)
                        && Math.abs(pos.getY()) + Math.abs(pos.getX()) <= 2
                        && Math.abs(pos.getZ()) + Math.abs(pos.getX()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

        ENERGY_PROVIDER_OFFSETS_WEST = BlockPos
                .stream(0, -2, -2, 2, 2, 2)
                .filter(pos -> (pos.getY() == 0 || pos.getZ() == 0)
                        && Math.abs(pos.getY()) + Math.abs(pos.getX()) <= 2
                        && Math.abs(pos.getZ()) + Math.abs(pos.getX()) <= 2)
                .map(BlockPos::toImmutable)
                .toList();

    }


}
