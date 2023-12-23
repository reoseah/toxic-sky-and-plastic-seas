package io.github.reoseah.toxsky;

import io.github.reoseah.toxsky.block.FloatingGarbageBlock;
import io.github.reoseah.toxsky.structure.GarbageIslandPiece;
import io.github.reoseah.toxsky.structure.GarbageIslandStructure;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToxSky implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("toxsky");

    public static final Block FLOATING_GARBAGE = new FloatingGarbageBlock(Block.Settings.create());
    public static final Block GARBAGE_BLOCK = new Block(Block.Settings.create().strength(2F));

    public static final Item PLASTIC_GARBAGE = new PlaceableOnWaterItem(FLOATING_GARBAGE, new Item.Settings()) {
        @Override
        public String getTranslationKey() {
            return this.getOrCreateTranslationKey();
        }
    };

    @Override
    public void onInitialize() {
        LOGGER.info("Poisoning atmosphere and polluting oceans!");

        Registry.register(Registries.BLOCK, "toxsky:floating_garbage", FLOATING_GARBAGE);
        Registry.register(Registries.BLOCK, "toxsky:garbage_block", GARBAGE_BLOCK);

        Registry.register(Registries.ITEM, "toxsky:garbage_block", new BlockItem(GARBAGE_BLOCK, new Item.Settings()));
        Registry.register(Registries.ITEM, "toxsky:plastic_garbage", PLASTIC_GARBAGE);

        ItemGroup group = FabricItemGroup.builder() //
                .displayName(Text.translatable("itemGroup.toxsky")) //
                .icon(PLASTIC_GARBAGE::getDefaultStack) //
                .entries((context, entries) -> {
                    entries.add(GARBAGE_BLOCK);
                    entries.add(PLASTIC_GARBAGE);
                }) //
                .build();

        Registry.register(Registries.ITEM_GROUP, "toxsky:main", group);

        Registry.register(Registries.STRUCTURE_TYPE, "toxsky:garbage_island", GarbageIslandStructure.TYPE);

        Registry.register(Registries.STRUCTURE_PIECE, "toxsky:garbage_island_center", GarbageIslandPiece.TYPE);

    }
}