package io.github.reoseah.toxsky.structure;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class AcidPoolPiece extends ShiftableStructurePiece {
    public static final StructurePieceType TYPE = AcidPoolPiece::new;

    protected final int seed;

    public AcidPoolPiece(BlockPos pos, Random random) {
        super(TYPE, pos.getX(), 64, pos.getZ(), 16, 8, 16, Direction.NORTH);

        this.seed = random.nextInt();
    }

    public AcidPoolPiece(StructureContext context, NbtCompound data) {
        super(TYPE, data);
        this.seed = data.getInt("seed");
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound data) {
        data.putInt("seed", this.seed);
    }

    @Override
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        if (!this.adjustToMinHeight(world, /*chunkBox,*/ 0)) {
            return;
        }

        this.addBlock(world, Blocks.BEDROCK.getDefaultState(), 8, 4, 8, chunkBox);

//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                this.addBlock(world, Blocks.WATER.getDefaultState(), x, 3, z, chunkBox);
//            }
//        }
    }

    @Override
    protected boolean canAddBlock(WorldView world, int x, int y, int z, BlockBox box) {
//        System.out.println("!AcidPoolPiece.canAddBlock");

        BlockState state = this.getBlockAt(world, x, y, z, box);
        return !state.isIn(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}
