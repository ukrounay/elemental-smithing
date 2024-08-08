package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class EnergyOverflowStatusEffect extends ModHealthBoostStatusEffect {

    public EnergyOverflowStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, 5, 2);
    }
}
