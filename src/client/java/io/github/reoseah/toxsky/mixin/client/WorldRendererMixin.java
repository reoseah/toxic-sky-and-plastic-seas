package io.github.reoseah.toxsky.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.reoseah.toxsky.ExtendedClientWorld;
import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyArg(method = "renderWeather", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"), index = 1)
    private Identifier changeTexture(Identifier texture) {
        World world = this.client.world;
        if (((ExtendedClientWorld) world).shouldConvertRainToAcidRain()) {
            texture = new Identifier("toxsky", "textures/environment/acid_rain.png");
        }
        return texture;
    }
}
