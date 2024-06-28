package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.damage.ModDamageTypes;
import net.ukrounay.elementalsmithing.item.ModToolMaterial;

public class ElementalSwordItem extends SwordItem {

    private final Element element;
    private final Enchantment enchantment;
//    private final int maxBonusDamage;

    public ElementalSwordItem(Element element, int attackDamage, float attackSpeed, Settings settings, Enchantment enchantment) {
        super(ModToolMaterial.ELEMENTAL, attackDamage, attackSpeed, settings);
        this.element = element;
        this.enchantment = enchantment;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getDamage() > stack.getMaxDamage()) {
            float fullness = (float) (stack.getMaxDamage() - stack.getDamage()) /  stack.getMaxDamage();
            target.damage(ModDamageTypes.vanillaPlayerAttack(attacker), 20 * fullness);
            stack.setDamage(stack.getDamage() + 1);
        }
        return true;
    }
//
//    @Override
//    public float getAttackDamage() {
//        return super.getAttackDamage() + ;
//    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        stack.addEnchantment(enchantment, 5);
    }
}
