package io.github.reoseah.toxsky.structure;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class GarbageIslandCenterPiece extends StructurePiece {
    public static final StructurePieceType TYPE = GarbageIslandCenterPiece::new;

    public GarbageIslandCenterPiece(BlockPos pos, Random random) {
        super(TYPE, 0, new BlockBox(pos).expand(8));
    }

    public GarbageIslandCenterPiece(StructureContext context, NbtCompound data) {
        super(TYPE, data);
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound nbt) {

    }

    @Override
    public void generate(StructureWorldAccess world, //
                         StructureAccessor structures, //
                         ChunkGenerator chunkGenerator, //
                         Random random, //
                         BlockBox chunkBox, //
                         ChunkPos chunkPos, //
                         BlockPos pivot) {
        BlockPos center = this.boundingBox.getCenter();

        this.addBlock(world, ToxSky.GARBAGE_BLOCK.getDefaultState(), center.getX(), center.getY(), center.getZ(), chunkBox);
    }
}
