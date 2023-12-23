package io.github.reoseah.toxsky.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class GarbageIslandStructure extends Structure {
    public static final Codec<GarbageIslandStructure> CODEC = RecordCodecBuilder //
            .create(instance -> instance.group(Structure.configCodecBuilder(instance)) //
                    .apply(instance, GarbageIslandStructure::new));

    public static final StructureType<GarbageIslandStructure> TYPE = () -> CODEC;

    public GarbageIslandStructure(Structure.Config settings) {
        super(settings);
    }

    @Override
    public StructureType<?> getType() {
        return TYPE;
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunk = context.chunkPos();
        ChunkRandom random = context.random();
        BlockPos pos = chunk.getBlockPos(0, context.chunkGenerator().getSeaLevel() - 1, 0);

        return Optional.of(new StructurePosition(pos, Either.left(builder -> {
            StructurePiece start = new GarbageIslandPiece(0, pos, random.split());
            builder.addPiece(start);
            start.fillOpenings(start, builder, random.split());
        })));
    }
}
