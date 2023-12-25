package io.github.reoseah.toxsky;

import io.github.reoseah.toxsky.block.FloatingGarbageBlock;
import io.github.reoseah.toxsky.item.RecycledPlasticArmorItem;
import io.github.reoseah.toxsky.structure.FloatingGarbagePiece;
import io.github.reoseah.toxsky.structure.GarbageIslandPiece;
import io.github.reoseah.toxsky.structure.GarbageIslandStructure;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToxSky implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("toxsky");

    public static final Block FLOATING_GARBAGE = new FloatingGarbageBlock(Block.Settings.create());
    public static final Block GARBAGE_BLOCK = new Block(Block.Settings.create().strength(2F));
    public static final Block SEAWEED_GARBAGE_BLOCK = new Block(Block.Settings.create().strength(2F));

    public static final Item PLASTIC_GARBAGE = new PlaceableOnWaterItem(FLOATING_GARBAGE, new Item.Settings()) {
        @Override
        public String getTranslationKey() {
            return this.getOrCreateTranslationKey();
        }
    };
    public static final Item SHREDDED_PLASTIC = new Item(new Item.Settings());
    public static final Item RECYCLED_PLASTIC = new Item(new Item.Settings());
    public static final Item RECYCLED_PLASTIC_HELMET = new RecycledPlasticArmorItem(ArmorItem.Type.HELMET, new Item.Settings());
    public static final Item RECYCLED_PLASTIC_CHESTPLATE = new RecycledPlasticArmorItem(ArmorItem.Type.CHESTPLATE, new Item.Settings());

    @Override
    public void onInitialize() {
        LOGGER.info("Poisoning atmosphere and polluting oceans!");

        Registry.register(Registries.BLOCK, "toxsky:floating_garbage", FLOATING_GARBAGE);
        Registry.register(Registries.BLOCK, "toxsky:garbage_block", GARBAGE_BLOCK);
        Registry.register(Registries.BLOCK, "toxsky:seaweed_garbage_block", SEAWEED_GARBAGE_BLOCK);

        Registry.register(Registries.ITEM, "toxsky:garbage_block", new BlockItem(GARBAGE_BLOCK, new Item.Settings()));
        Registry.register(Registries.ITEM, "toxsky:seaweed_garbage_block", new BlockItem(SEAWEED_GARBAGE_BLOCK, new Item.Settings().recipeRemainder(GARBAGE_BLOCK.asItem())));
        Registry.register(Registries.ITEM, "toxsky:plastic_garbage", PLASTIC_GARBAGE);
        Registry.register(Registries.ITEM, "toxsky:shredded_plastic", SHREDDED_PLASTIC);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic", RECYCLED_PLASTIC);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_helmet", RECYCLED_PLASTIC_HELMET);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_chestplate", RECYCLED_PLASTIC_CHESTPLATE);

        ItemGroup group = FabricItemGroup.builder() //
                .displayName(Text.translatable("itemGroup.toxsky")) //
                .icon(PLASTIC_GARBAGE::getDefaultStack) //
                .entries((context, entries) -> {
                    entries.add(GARBAGE_BLOCK);
                    entries.add(SEAWEED_GARBAGE_BLOCK);
                    entries.add(PLASTIC_GARBAGE);
                    entries.add(SHREDDED_PLASTIC);
                    entries.add(RECYCLED_PLASTIC);
                    entries.add(RECYCLED_PLASTIC_HELMET);
                    entries.add(RECYCLED_PLASTIC_CHESTPLATE);
                }) //
                .build();

        Registry.register(Registries.ITEM_GROUP, "toxsky:main", group);

        Registry.register(Registries.STRUCTURE_TYPE, "toxsky:garbage_island", GarbageIslandStructure.TYPE);

        Registry.register(Registries.STRUCTURE_PIECE, "toxsky:garbage_island", GarbageIslandPiece.TYPE);
        Registry.register(Registries.STRUCTURE_PIECE, "toxsky:floating_garbage", FloatingGarbagePiece.TYPE);
    }
}