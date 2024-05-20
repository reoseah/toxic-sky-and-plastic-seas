package io.github.reoseah.toxsky.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

@SuppressWarnings("deprecation")
public class FloatingGarbageBlock extends Block {
//    public static final MapCodec<FloatingGarbageBlock> CODEC = createCodec(FloatingGarbageBlock::new);

    public static final VoxelShape SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.5, 15.0);

    public FloatingGarbageBlock(Settings settings) {
        super(settings);
    }

//    public MapCodec<FloatingGarbageBlock> getCodec() {
//        return CODEC;
//    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState floor = world.getBlockState(pos.down());
        FluidState floorFluid = world.getFluidState(pos.down());
        FluidState fluid = world.getFluidState(pos);
        return (floorFluid.getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) //
                && fluid.getFluid() == Fluids.EMPTY;
    }
}
