package net.ukrounay.elementalsmithing.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> VITAL_ENERGY_DRAIN_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(ElementalSmithing.MOD_ID, "vital_energy_drain_damage_type"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static DamageSource vanillaPlayerAttack(LivingEntity attacker) {
        return new DamageSources(attacker.getWorld().getRegistryManager()).playerAttack((PlayerEntity) attacker);
    }
}
