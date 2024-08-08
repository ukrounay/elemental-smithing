package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ModHealthBoostStatusEffect extends StatusEffect {

    private boolean shouldHealNow = false;
    private final int healingInterval;
    public final float healingAmount;

    protected ModHealthBoostStatusEffect(StatusEffectCategory category, int color, int healingInterval, float healingAmount) {
        super(category, color);
        this.healingInterval = healingInterval;
        this.healingAmount = healingAmount;
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity.getHealth() > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.heal(healingAmount);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        this.shouldHealNow = duration % this.healingInterval == 0;
        return true;
    }
}
