package net.ukrounay.elementalsmithing.item;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.ukrounay.elementalsmithing.util.ModTags;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {

    STEEL(MiningLevels.DIAMOND, 890, 8.0f, 2.5f, 15, () -> Ingredient.ofItems(ModItems.STEEL_INGOT)),
    DAMASK_STEEL(MiningLevels.NETHERITE, 3280, 9.0f, 3.5f, 25, () -> Ingredient.ofItems(ModItems.STEEL_INGOT)),

    BLESSED_CRYSTAL(MiningLevels.DIAMOND,560,11.0f,2f, 10, () -> Ingredient.fromTag(ModTags.Items.CRYSTALS)),
    CURSED_CRYSTAL(MiningLevels.DIAMOND,120,4.0f,6f, 10, () -> Ingredient.fromTag(ModTags.Items.CRYSTALS)),
    PURE_CRYSTAL(MiningLevels.DIAMOND,2560,10.0f,4.5f, 10, () -> Ingredient.fromTag(ModTags.Items.CRYSTALS)),

    AMORPHOUS(MiningLevels.HAND, 250, 2.0f, 10.0f, 30, () -> Ingredient.ofItems(Items.GLASS));



    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
