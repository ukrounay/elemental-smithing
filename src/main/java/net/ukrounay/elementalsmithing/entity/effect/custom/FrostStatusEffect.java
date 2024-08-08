package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class FrostStatusEffect extends ModHealthBoostStatusEffect {

    public FrostStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, 10, 1);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if(entity.isFrozen()) {
            entity.setFrozenTicks(0);
        }
    }
}
