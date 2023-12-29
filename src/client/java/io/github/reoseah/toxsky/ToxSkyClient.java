package io.github.reoseah.toxsky;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public class ToxSkyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), //
                ToxSky.FLOATING_GARBAGE);

        ClientPlayNetworking.registerGlobalReceiver(new Identifier("toxsky:convert_rain_to_acid_rain"), (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                boolean value = buf.readBoolean();

                ClientWorld world = client.world;
                if (world != null) {
                    ((ExtendedClientWorld) world).setConvertRainToAcidRain(value);
                }
            });
        });
    }
}