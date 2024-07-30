package net.ukrounay.elementalsmithing.item.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.item.ModArmorMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModArmorItem extends ArmorItem {

    private static final Map<ArmorMaterial, List<StatusEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<StatusEffectInstance>>())
                    .put(ModArmorMaterials.REINFORCED_DAMASK_STEEL, List.of(
                            new StatusEffectInstance(
                                StatusEffects.RESISTANCE,
                                40,2,
                                false, false, false
                            ),
                            new StatusEffectInstance(
                                StatusEffects.SLOWNESS,
                                40,0,
                                false, false, false
                            )
                    ))
                    .put(ModArmorMaterials.DAMASK_STEEL, List.of(
                            new StatusEffectInstance(
                                    StatusEffects.RESISTANCE,
                                    40,1,
                                    false, false, false
                            )
                    ))
                    .put(ModArmorMaterials.STEEL, List.of(
                            new StatusEffectInstance(
                                    StatusEffects.RESISTANCE,
                                    40,0,
                                    false, false, false
                            )
                    ))
                    .build();

    public ModArmorItem(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient() && entity instanceof PlayerEntity player && hasFullSuitOfArmorOn(player))
            evaluateArmorEffects(player);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<ArmorMaterial, List<StatusEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<StatusEffectInstance> mapStatusEffects = entry.getValue();
            if(hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectsForMaterial(player, mapStatusEffects);
            }
        }
    }

    private void addStatusEffectsForMaterial(PlayerEntity player, List<StatusEffectInstance> effects) {
        for (StatusEffectInstance effect : effects) {
            if(!player.hasStatusEffect(effect.getEffectType())) {
                player.addStatusEffect(new StatusEffectInstance(effect));
            }
        }
    }

    private boolean hasFullSuitOfArmorOn(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        ItemStack leggings = player.getInventory().getArmorStack(1);
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        ItemStack helmet = player.getInventory().getArmorStack(3);

        return !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, PlayerEntity player) {
        for (ItemStack armorStack: player.getInventory().armor)
            if(!(armorStack.getItem() instanceof ArmorItem)) return false;

        ArmorItem boots = ((ArmorItem)player.getInventory().getArmorStack(0).getItem());
        ArmorItem leggings = ((ArmorItem)player.getInventory().getArmorStack(1).getItem());
        ArmorItem chestplate = ((ArmorItem)player.getInventory().getArmorStack(2).getItem());
        ArmorItem helmet = ((ArmorItem)player.getInventory().getArmorStack(3).getItem());

        return helmet.getMaterial() == material && chestplate.getMaterial() == material &&
                leggings.getMaterial() == material && boots.getMaterial() == material;
    }
}
