package io.github.reoseah.toxsky;

import io.github.reoseah.toxsky.block.FloatingGarbageBlock;
import io.github.reoseah.toxsky.enchantment.AcidproofEnchantment;
import io.github.reoseah.toxsky.item.RecycledPlasticArmorItem;
import io.github.reoseah.toxsky.structure.FloatingGarbagePiece;
import io.github.reoseah.toxsky.structure.GarbageIslandPiece;
import io.github.reoseah.toxsky.structure.GarbageIslandStructure;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToxSky implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("toxsky");

    public static final Block FLOATING_GARBAGE = new FloatingGarbageBlock(Block.Settings.create().mapColor(MapColor.LIGHT_BLUE));
    public static final Block GARBAGE_BLOCK = new Block(Block.Settings.create().mapColor(MapColor.PINK).strength(2F));
    public static final Block SEAWEED_GARBAGE_BLOCK = new Block(Block.Settings.create().mapColor(MapColor.GREEN).strength(2F));
    public static final Block RECYCLED_PLASTIC_BLOCK = new Block(Block.Settings.create().mapColor(DyeColor.ORANGE).strength(2F));

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
    public static final Item RECYCLED_PLASTIC_LEGGINGS = new RecycledPlasticArmorItem(ArmorItem.Type.LEGGINGS, new Item.Settings());
    public static final Item RECYCLED_PLASTIC_BOOTS = new RecycledPlasticArmorItem(ArmorItem.Type.BOOTS, new Item.Settings());

    public static final Enchantment ACIDPROOF = new AcidproofEnchantment();

    public static final TagKey<Item> IMMUNE_TO_ACID_RAIN = TagKey.of(RegistryKeys.ITEM, new Identifier("toxsky", "immune_to_acid_rain"));
    public static final TagKey<Item> CANNOT_HAVE_ACIDPROOF_ENCHANTMENT = TagKey.of(RegistryKeys.ITEM, new Identifier("toxsky", "cannot_have_acidproof_enchantment"));

    public static final TagKey<EntityType<?>> IMMUNE_TO_ACID_RAIN_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("toxsky", "immune_to_acid_rain_entities"));
    
    public static final GameRules.Key<GameRules.BooleanRule> CONVERT_RAIN_TO_ACID_RAIN = GameRuleRegistry.register("convertRainToAcidRain", //
            GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false, (server, rule) -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(rule.get());

                server.getPlayerManager().sendToAll(ServerPlayNetworking.createS2CPacket(new Identifier("toxsky:convert_rain_to_acid_rain"), buf));
            }));

    @Override
    public void onInitialize() {
        LOGGER.info("Poisoning atmosphere and polluting oceans!");

        Registry.register(Registries.BLOCK, "toxsky:floating_garbage", FLOATING_GARBAGE);
        Registry.register(Registries.BLOCK, "toxsky:garbage_block", GARBAGE_BLOCK);
        Registry.register(Registries.BLOCK, "toxsky:seaweed_garbage_block", SEAWEED_GARBAGE_BLOCK);
        Registry.register(Registries.BLOCK, "toxsky:recycled_plastic_block", RECYCLED_PLASTIC_BLOCK);

        Registry.register(Registries.ITEM, "toxsky:garbage_block", new BlockItem(GARBAGE_BLOCK, new Item.Settings()));
        Registry.register(Registries.ITEM, "toxsky:seaweed_garbage_block", new BlockItem(SEAWEED_GARBAGE_BLOCK, new Item.Settings().recipeRemainder(GARBAGE_BLOCK.asItem())));
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_block", new BlockItem(RECYCLED_PLASTIC_BLOCK, new Item.Settings()));
        Registry.register(Registries.ITEM, "toxsky:plastic_garbage", PLASTIC_GARBAGE);
        Registry.register(Registries.ITEM, "toxsky:shredded_plastic", SHREDDED_PLASTIC);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic", RECYCLED_PLASTIC);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_helmet", RECYCLED_PLASTIC_HELMET);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_chestplate", RECYCLED_PLASTIC_CHESTPLATE);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_leggings", RECYCLED_PLASTIC_LEGGINGS);
        Registry.register(Registries.ITEM, "toxsky:recycled_plastic_boots", RECYCLED_PLASTIC_BOOTS);

        Registry.register(Registries.ENCHANTMENT, "toxsky:acidproof", ACIDPROOF);

        ItemGroup group = FabricItemGroup.builder() //
                .displayName(Text.translatable("itemGroup.toxsky")) //
                .icon(PLASTIC_GARBAGE::getDefaultStack) //
                .entries((context, entries) -> {
                    entries.add(GARBAGE_BLOCK);
                    entries.add(SEAWEED_GARBAGE_BLOCK);
                    entries.add(RECYCLED_PLASTIC_BLOCK);

                    entries.add(PLASTIC_GARBAGE);
                    entries.add(SHREDDED_PLASTIC);
                    entries.add(RECYCLED_PLASTIC);
                    entries.add(RECYCLED_PLASTIC_HELMET);
                    entries.add(RECYCLED_PLASTIC_CHESTPLATE);
                    entries.add(RECYCLED_PLASTIC_LEGGINGS);
                    entries.add(RECYCLED_PLASTIC_BOOTS);

                    entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(ACIDPROOF, 1)));
                }) //
                .build();
        Registry.register(Registries.ITEM_GROUP, "toxsky:main", group);

        Registry.register(Registries.STRUCTURE_TYPE, "toxsky:garbage_island", GarbageIslandStructure.TYPE);

        Registry.register(Registries.STRUCTURE_PIECE, "toxsky:garbage_island", GarbageIslandPiece.TYPE);
        Registry.register(Registries.STRUCTURE_PIECE, "toxsky:floating_garbage", FloatingGarbagePiece.TYPE);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBoolean(server.getGameRules().get(CONVERT_RAIN_TO_ACID_RAIN).get());

            ServerPlayNetworking.send(handler.player, new Identifier("toxsky:convert_rain_to_acid_rain"), buf);
        });
    }
}