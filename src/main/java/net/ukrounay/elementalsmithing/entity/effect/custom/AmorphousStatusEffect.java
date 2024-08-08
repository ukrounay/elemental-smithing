package net.ukrounay.elementalsmithing.entity.effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffects;

public class AmorphousStatusEffect extends ModHealthBoostStatusEffect {

    public AmorphousStatusEffect(StatusEffectCategory category, int color) {
        super(category, color, 2, 2);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if (!entity.getWorld().isClient && entity instanceof PlayerEntity player) {
            player.getAbilities().allowFlying = true;
            player.getAbilities().flying = true;
            player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (!entity.getWorld().isClient && entity instanceof PlayerEntity player
            && !player.isCreative()
            && !player.isSpectator()
        ) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
            player.sendAbilitiesUpdate();
        }
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOW_FALLING, 120));
    }

}
