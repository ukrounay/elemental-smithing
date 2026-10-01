package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;

public class ModEnchantedPickaxeItem extends PickaxeItem {

    private final Enchantment power;
    private final int powerLevel;

    public ModEnchantedPickaxeItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Enchantment power, int powerLevel, Settings settings) {
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
