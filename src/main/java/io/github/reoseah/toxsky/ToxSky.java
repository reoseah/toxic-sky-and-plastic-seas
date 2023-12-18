package io.github.reoseah.toxsky;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToxSky implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("toxsky");

    public static final Item PLASTIC_GARBAGE = new Item(new Item.Settings());

    @Override
    public void onInitialize() {
		LOGGER.info("Poisoning atmosphere and polluting oceans!");

        Registry.register(Registries.ITEM, "toxsky:plastic_garbage", PLASTIC_GARBAGE);

        ItemGroup group = FabricItemGroup.builder() //
                .displayName(Text.translatable("itemGroup.toxsky")) //
                .icon(PLASTIC_GARBAGE::getDefaultStack) //
				.entries((context, entries) -> {
					entries.add(PLASTIC_GARBAGE);
				})
                .build();

		Registry.register(Registries.ITEM_GROUP, "toxsky:main", group);
    }
}