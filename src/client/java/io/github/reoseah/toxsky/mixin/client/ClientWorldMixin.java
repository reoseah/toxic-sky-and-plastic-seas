package io.github.reoseah.toxsky.mixin.client;

import io.github.reoseah.toxsky.ExtendedClientWorld;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements ExtendedClientWorld {
    private boolean toxsky$convertRainToAcidRain;

    @Override
    public boolean shouldConvertRainToAcidRain() {
        return this.toxsky$convertRainToAcidRain;
    }

    @Override
    public void setConvertRainToAcidRain(boolean value) {
        this.toxsky$convertRainToAcidRain = value;
    }
}
