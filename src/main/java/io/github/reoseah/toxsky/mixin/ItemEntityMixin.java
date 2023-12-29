package io.github.reoseah.toxsky.mixin;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
        if (!this.getWorld().isClient //
                && !this.isRemoved() //
                && this.getStack().isDamageable() //
                && !this.getStack().isIn(ToxSky.IMMUNE_TO_ACID_RAIN) //
                && this.getWorld().getTime() % 20 == 0 //
                && this.getWorld().isRaining() //
                && this.getWorld().getGameRules().getBoolean(ToxSky.CONVERT_RAIN_TO_ACID_RAIN) //
                && this.isBeingRainedOn()) {
            ItemStack stack = this.getStack();
            stack.setDamage(stack.getDamage() + 1);
            if (stack.getDamage() >= stack.getMaxDamage()) {
                this.remove(RemovalReason.KILLED);
            } else {
                this.setStack(stack);
            }
        }
    }
}
