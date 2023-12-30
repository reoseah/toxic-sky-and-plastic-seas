package io.github.reoseah.toxsky.mixin;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    public void tickMovement(CallbackInfo info) {
        LivingEntity living = (LivingEntity) (Object) this;
        World world = this.getWorld();
        BlockPos pos = this.getBlockPos();
        if (!world.isClient //
                && world.getTime() % 20 == 0 //
                && world.isRaining() //
                && world.getGameRules().getBoolean(ToxSky.CONVERT_RAIN_TO_ACID_RAIN) //
                && !this.getType().isIn(ToxSky.IMMUNE_TO_ACID_RAIN_ENTITIES) //
                && this.isBeingRainedOn() //
                && world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.RAIN) {

            this.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = living.getEquippedStack(slot);
                if (stack.isDamageable() //
                        && !stack.isIn(ToxSky.IMMUNE_TO_ACID_RAIN) //
                        && EnchantmentHelper.getLevel(ToxSky.ACIDPROOF, stack) == 0) {
                    stack.damage(1, living, (entity) -> entity.sendEquipmentBreakStatus(slot));
                }
            }
        }
    }
}
