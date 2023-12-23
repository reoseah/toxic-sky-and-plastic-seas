package io.github.reoseah.toxsky.structure;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class GarbageIslandPiece extends StructurePiece {
    public static final StructurePieceType TYPE = GarbageIslandPiece::new;

    protected final int seed;

    public GarbageIslandPiece(int generation, BlockPos pos, Random random) {
        super(TYPE, generation, new BlockBox(pos).expand(8));
        this.seed = random.nextInt();
    }

    public GarbageIslandPiece(StructureContext context, NbtCompound data) {
        super(TYPE, data);
        this.seed = data.getInt("seed");
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound data) {
        data.putInt("seed", this.seed);
    }

    @Override
    public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
        if (this.chainLength >= 1) {
            return;
        }

        float angle = random.nextFloat() * 2 * MathHelper.PI;
        int count = random.nextBetween(3, 5);

        for (int i = 0; i < count; i++) {
            BlockPos pos = this.getBoundingBox().getCenter();

            int distance = random.nextBetween(16, 24);
            float angleOffset = random.nextFloat() - 0.5F;
            int x = pos.getX() + Math.round(MathHelper.cos(angle + angleOffset) * distance);
            int z = pos.getZ() + Math.round(MathHelper.sin(angle + angleOffset) * distance);
            int y = pos.getY();
            BlockPos offset = new BlockPos(x, y, z);
            StructurePiece piece = new GarbageIslandPiece(this.chainLength + 1, offset, random.split());
            holder.addPiece(piece);
            piece.fillOpenings(start, holder, random.split());
            angle += 2 * MathHelper.PI / count;
        }

        StructurePiece piece = new FloatingGarbagePiece(this.chainLength + 1, this.getBoundingBox().getCenter(), random.split());
        holder.addPiece(piece);
        piece.fillOpenings(start, holder, random.split());
    }

    @Override
    public void generate(StructureWorldAccess world, //
                         StructureAccessor structures, //
                         ChunkGenerator chunkGenerator, //
                         Random r, //
                         BlockBox chunkBox, //
                         ChunkPos chunkPos, //
                         BlockPos pivot) {
        Random random = Random.create(this.seed);

        int[][] map = new int[16][16];
        map[8][8] = 1;

        for (int i = 0; i < 200; i++) {
            int x = random.nextInt(9) + random.nextInt(8);
            int z = random.nextInt(9) + random.nextInt(8);

            int neighbors = 0;
            if (x > 0 && map[x - 1][z] == 1) {
                neighbors++;
            }
            if (x < 15 && map[x + 1][z] == 1) {
                neighbors++;
            }
            if (z > 0 && map[x][z - 1] == 1) {
                neighbors++;
            }
            if (z < 15 && map[x][z + 1] == 1) {
                neighbors++;
            }
            if (neighbors == 0 || neighbors == 4) {
                continue;
            }
            map[x][z] = 1;
        }

        int[][] corners = new int[16][16];
        int[][] floaties = new int[16][16];

        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                int orthogonalNeighbors = 0, diagonalNeighbors = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dz == 0) {
                            continue;
                        }
                        if (map[x + dx][z + dz] == 1) {
                            if (dx == 0 || dz == 0) {
                                orthogonalNeighbors++;
                            } else {
                                diagonalNeighbors++;
                            }
                        }
                    }
                }
                if (orthogonalNeighbors == 0 && diagonalNeighbors == 1) {
                    if (random.nextInt(4) == 0) {
                        corners[x][z] = 1;
                    } else {
                        floaties[x][z] = 1;
                    }
                } else if (orthogonalNeighbors >= 2) {
                    floaties[x][z] = 1;
                } else if (orthogonalNeighbors == 0 && diagonalNeighbors == 0 && random.nextInt(8) == 0) {
                    floaties[x][z] = 1;
                }
            }
        }

        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                BlockPos pos = new BlockPos( //
                        this.getBoundingBox().getMinX(), //
                        this.getBoundingBox().getCenter().getY(), //
                        this.getBoundingBox().getMinZ() //
                );
                int x = pos.getX() + dx;
                int z = pos.getZ() + dz;
                int y = pos.getY();
                if (map[dx][dz] == 1 || corners[dx][dz] == 1) {
                    this.addBlock(world, ToxSky.GARBAGE_BLOCK.getDefaultState(), x, y, z, chunkBox);
                } else if (floaties[dx][dz] == 1) {
                    this.addBlock(world, ToxSky.FLOATING_GARBAGE.getDefaultState(), x, y + 1, z, chunkBox);
                }
            }
        }
    }

    @Override
    protected boolean canAddBlock(WorldView world, int x, int y, int z, BlockBox box) {
        BlockState state = this.getBlockAt(world, x, y, z, box);
        return state.isAir() || state.isReplaceable() || state.isOf(Blocks.WATER);
    }
}
