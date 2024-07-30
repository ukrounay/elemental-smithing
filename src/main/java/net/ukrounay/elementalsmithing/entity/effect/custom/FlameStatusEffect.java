package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FlameStatusEffect extends StatusEffect {


    public FlameStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.isOnFire()) {
            entity.setFireTicks(0);
            entity.setOnFire(false);
        }
        if(!entity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE))
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1, 3, true, false, false));
    }
}
