package io.github.reoseah.toxsky.item;

import io.github.reoseah.toxsky.ToxSky;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class RecycledPlasticArmorItem extends ArmorItem {
    public static final ArmorMaterial MATERIAL = new Material();

    public RecycledPlasticArmorItem(Type type, Settings settings) {
        super(MATERIAL, type, settings);
    }

    public static class Material implements ArmorMaterial {
        @Override
        public int getDurability(Type type) {
            return 29 * ArmorMaterials.BASE_DURABILITY.get(type);
        }

        @Override
        public int getProtection(Type type) {
            return switch (type) {
                case BOOTS, HELMET -> 2;
                case CHESTPLATE -> 5;
                case LEGGINGS -> 4;
            };
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(ToxSky.RECYCLED_PLASTIC);
        }

        @Override
        public String getName() {
            return "recycled_plastic";
        }

        @Override
        public float getToughness() {
            return 2.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }
}
