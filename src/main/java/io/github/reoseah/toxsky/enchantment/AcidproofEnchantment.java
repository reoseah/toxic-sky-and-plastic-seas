package io.github.reoseah.toxsky.enchantment;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class AcidproofEnchantment extends Enchantment {
    public AcidproofEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public int getMinPower(int level) {
        return level * 25;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack) && !stack.isIn(ToxSky.CANNOT_HAVE_ACIDPROOF_ENCHANTMENT);
    }
}
