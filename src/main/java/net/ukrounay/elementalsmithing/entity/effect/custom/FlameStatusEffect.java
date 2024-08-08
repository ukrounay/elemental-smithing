package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffectInstance;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffects;

public class FlameStatusEffect extends ModHealthBoostStatusEffect {

    private static final StatusEffectInstance additionalEffect =
        new ModStatusEffectInstance(StatusEffects.FIRE_RESISTANCE,
        60,3,true, false, false);


    public FlameStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, 10, 1);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if(entity.isOnFire()) {
            entity.setFireTicks(0);
            entity.setOnFire(false);
        }

        StatusEffectInstance currentEffect = entity.getStatusEffect(additionalEffect.getEffectType());
        if(currentEffect instanceof ModStatusEffectInstance modEffect) {
            if(modEffect.isDurationBelow(20))
                modEffect.updateDuration(additionalEffect.getDuration());
        } else entity.addStatusEffect(new ModStatusEffectInstance(additionalEffect));

    }

}
