package io.github.reoseah.toxsky.mixin;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    public void tickMovement(CallbackInfo info) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (!this.getWorld().isClient //
                && this.getWorld().getTime() % 20 == 0 //
                && this.getWorld().isRaining() //
                && this.getWorld().getGameRules().getBoolean(ToxSky.CONVERT_RAIN_TO_ACID_RAIN) //
                && this.isBeingRainedOn()) {
            this.damage(this.getWorld().getDamageSources().cactus(), 1F); // TODO damage source
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = living.getEquippedStack(slot);
                if (stack.isDamageable() //
                        && !stack.isIn(ToxSky.IMMUNE_TO_ACID_RAIN)) {
                    stack.damage(1, living, (entity) -> entity.sendEquipmentBreakStatus(slot));
                }
            }
        }
    }
}
