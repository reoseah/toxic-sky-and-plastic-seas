package io.github.reoseah.toxsky.structure;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class FloatingGarbagePiece extends StructurePiece {
    public static final StructurePieceType TYPE = FloatingGarbagePiece::new;

    protected final int seed;

    public FloatingGarbagePiece(int generation, BlockPos pos, Random random) {
        super(TYPE, generation, new BlockBox(pos).expand(32));
        this.seed = random.nextInt();
    }

    public FloatingGarbagePiece(StructureContext context, NbtCompound data) {
        super(TYPE, data);
        this.seed = data.getInt("seed");
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound data) {
        data.putInt("seed", this.seed);
    }

    @Override
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
        BlockPos center = this.getBoundingBox().getCenter();
        int y = center.getY() + 1;
        for (int i = 0; i < 100; i++) {
            int dx = random.nextInt(16) + random.nextInt(48) - 32;
            int dz = random.nextInt(16) + random.nextInt(48) - 32;

            int x = center.getX() + dx;
            int z = center.getZ() + dz;

            this.addBlock(world, ToxSky.FLOATING_GARBAGE.getDefaultState(), x, y, z, chunkBox);
        }
    }

    @Override
    protected boolean canAddBlock(WorldView world, int x, int y, int z, BlockBox box) {
        BlockState state = this.getBlockAt(world, x, y, z, box);
        return state.isAir() || state.isReplaceable() || state.isOf(Blocks.WATER);
    }
}
