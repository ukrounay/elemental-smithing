package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffectInstance;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffects;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.Element;

public class AmorphousSwordItem extends ElementalSwordItem {

    private static final StatusEffectInstance effect =
            new ModStatusEffectInstance(ModStatusEffects.AMORPHOUSNESS, 40);


    public AmorphousSwordItem(Element element, int attackDamage, int bonusDamage, float attackSpeed, int useCooldown, Enchantment enchantment, Settings settings) {
        super(element, attackDamage, bonusDamage, attackSpeed, useCooldown, enchantment, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if(!world.isClient() && entity instanceof PlayerEntity player && hasSwordInHand(player))
            grantSwordEffect(player);
    }

    private boolean hasSwordInHand(PlayerEntity player) {
        ItemStack stackInOffHand = player.getStackInHand(Hand.OFF_HAND);
        ItemStack stackInMainHand = player.getStackInHand(Hand.MAIN_HAND);
        return stackInOffHand.isOf(ModItems.AMORPHOUS_SWORD) || stackInMainHand.isOf(ModItems.AMORPHOUS_SWORD);
    }

    private void grantSwordEffect(PlayerEntity player) {
        StatusEffectInstance currentEffect = player.getStatusEffect(effect.getEffectType());
        if(currentEffect instanceof ModStatusEffectInstance modEffect) {
            if(modEffect.isDurationBelow(20))
                modEffect.updateDuration(effect.getDuration());
        } else player.addStatusEffect(new ModStatusEffectInstance(effect));
    }

}
