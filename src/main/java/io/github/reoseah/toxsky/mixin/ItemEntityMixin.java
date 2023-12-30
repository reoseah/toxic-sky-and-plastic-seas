package io.github.reoseah.toxsky.mixin;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract void setStack(ItemStack stack);

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        ItemStack stack = this.getStack();
        World world = this.getWorld();
        BlockPos pos = this.getBlockPos();
        if (!world.isClient //
                && world.getTime() % 20 == 0 //
                && !this.isRemoved() //
                && stack.isDamageable() //
                && !stack.isIn(ToxSky.IMMUNE_TO_ACID_RAIN) //
                && EnchantmentHelper.getLevel(ToxSky.ACIDPROOF, stack) == 0
                && world.isRaining() //
                && world.getGameRules().getBoolean(ToxSky.CONVERT_RAIN_TO_ACID_RAIN) //
                && this.isBeingRainedOn() //
                && world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.RAIN) {
            stack.setDamage(stack.getDamage() + 1);
            if (stack.getDamage() >= stack.getMaxDamage()) {
                this.remove(RemovalReason.KILLED);
            } else {
                this.setStack(stack);
            }
        }
    }
}
