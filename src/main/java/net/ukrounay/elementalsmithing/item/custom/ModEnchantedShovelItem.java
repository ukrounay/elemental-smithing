package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class ModEnchantedShovelItem extends ShovelItem {

    private final Enchantment power;
    private final int powerLevel;

    public ModEnchantedShovelItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Enchantment power, int powerLevel, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.power = power;
        this.powerLevel = powerLevel;
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.addEnchantment(power, powerLevel);
        return stack;
    }

}
