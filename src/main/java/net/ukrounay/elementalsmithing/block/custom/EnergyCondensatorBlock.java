package net.ukrounay.elementalsmithing.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import net.ukrounay.elementalsmithing.block.entity.ModBlockEntities;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyCondensatorBlock extends BlockWithEntity implements BlockEntityProvider {

    private static final VoxelShape SHAPE = VoxelShapes.union(
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

    public static final List<BlockPos> ENERGY_PROVIDER_OFFSETS = BlockPos.stream(-2, -2, -2, 2, 2, 2).filter(pos -> Math.abs(pos.getX()) != 1 && Math.abs(pos.getY()) != 1 && Math.abs(pos.getZ()) != 1).map(BlockPos::toImmutable).toList();


    public EnergyCondensatorBlock(Settings settings) {
        super(settings);
    }

    public static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
        return world.getBlockState(tablePos.add(providerOffset)).isIn(ModTags.Blocks.ENERGY_PROVIDER) && !world.getBlockState(tablePos.add(providerOffset.getX() / 2, providerOffset.getY(), providerOffset.getZ() / 2)).isIn(ModTags.Blocks.ENERGY_BARRIER);
    }

    public static float getPower(World world, BlockPos pos) {
        int number = 0;
        for (BlockPos blockPos : ENERGY_PROVIDER_OFFSETS)
            if (!canAccessPowerProvider(world, pos, blockPos))
                number++;
        return (float) number / ENERGY_PROVIDER_OFFSETS.size();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        for (BlockPos blockPos : ENERGY_PROVIDER_OFFSETS) {
            if (random.nextInt(8) != 0 || !canAccessPowerProvider(world, pos, blockPos)) continue;
            world.addParticle(ModParticles.ENERGY_PARTICLE,
                    pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5,
                    (double)((float)blockPos.getX() + random.nextFloat()) - 0.5,
                    (float)blockPos.getY() - random.nextFloat() - 1.0f,
                    (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5
            );
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (random.nextInt(4) == 0
                && blockEntity instanceof EnergyCondensatorBlockEntity
                && ((EnergyCondensatorBlockEntity) blockEntity).isCharging())
            world.addParticle(ModParticles.TRACING_ENERGY_PARTICLE,
                    pos.getX() + 0.5, pos.getY() + 1.6, pos.getZ() + 0.5,
                    0, 0, 0
            );
    }

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
        return new EnergyCondensatorBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {return;}
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnergyCondensatorBlockEntity) {
            ItemScatterer.spawn(world, pos, ((EnergyCondensatorBlockEntity)blockEntity).getItems());
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof EnergyCondensatorBlockEntity) {
            if (!world.isClient() && ((EnergyCondensatorBlockEntity)blockEntity).interact(player, hand)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient())
            return EnergyCondensatorBlock.checkType(type, ModBlockEntities.ENERGY_CONDENSATOR, EnergyCondensatorBlockEntity::tick);
        return null;
    }


}
