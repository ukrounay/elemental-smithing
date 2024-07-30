package net.ukrounay.elementalsmithing.entity.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.entity.effect.custom.AmorphousStatusEffect;
import net.ukrounay.elementalsmithing.entity.effect.custom.EnergyOverflowStatusEffect;
import net.ukrounay.elementalsmithing.entity.effect.custom.FlameStatusEffect;
import net.ukrounay.elementalsmithing.entity.effect.custom.FrostStatusEffect;

import java.util.UUID;

public class ModStatusEffects {

    public static final StatusEffect FLAME_EMBODIMENT = registerStatusEffect("flame_embodiment",
            new FlameStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFF5555)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, UUID.randomUUID().toString(), 4, EntityAttributeModifier.Operation.ADDITION)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, UUID.randomUUID().toString(), 0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );
    public static final StatusEffect FROST_EMBODIMENT = registerStatusEffect("frost_embodiment",
            new FrostStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFF55FFFF)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, UUID.randomUUID().toString(), 6, EntityAttributeModifier.Operation.ADDITION)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)

    );
    public static final StatusEffect VITAL_ENERGY_OVERFLOW = registerStatusEffect("vital_energy_overflow",
            new EnergyOverflowStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFA1515)
                .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, UUID.randomUUID().toString(), 10, EntityAttributeModifier.Operation.ADDITION)
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, UUID.randomUUID().toString(), 0.1f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)

    );
    public static final StatusEffect AMORPHOUSNESS = registerStatusEffect("amorphousness",
            new AmorphousStatusEffect(StatusEffectCategory.BENEFICIAL, 0xFFFF55FF)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, UUID.randomUUID().toString(), -20, EntityAttributeModifier.Operation.ADDITION)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, UUID.randomUUID().toString(), 0.5f, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)

    );


    private static StatusEffect registerStatusEffect(String name, StatusEffect statusEffect) {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(ElementalSmithing.MOD_ID, name), statusEffect);
        return statusEffect;
    }

    public static void registerModStatusEffects() {
        ElementalSmithing.LOGGER.info("Registering mod status effects for " + ElementalSmithing.MOD_ID);
    }
}
