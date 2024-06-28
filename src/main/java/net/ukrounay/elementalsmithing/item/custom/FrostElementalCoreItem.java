package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.entity.effect.StatusEffects;

public class FrostElementalCoreItem extends ElementalCoreItem {

    public FrostElementalCoreItem(Settings settings) {
        super(settings, Element.FROST, 20,20, StatusEffects.SLOW_FALLING);
    }

}
