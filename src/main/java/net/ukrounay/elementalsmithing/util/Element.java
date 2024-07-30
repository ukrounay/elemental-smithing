package net.ukrounay.elementalsmithing.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.entity.effect.ModStatusEffects;
import net.ukrounay.elementalsmithing.sound.ModSounds;

public enum Element {
    FLAME("flame", 0xFFFF5555, Formatting.RED, true, Blocks.FIRE, Blocks.OBSIDIAN, Blocks.MAGMA_BLOCK, Blocks.LAVA, SoundEvents.ITEM_FIRECHARGE_USE, SoundEvents.BLOCK_FIRE_EXTINGUISH, ModSounds.FLAME_CORE_COMPLETION, ModStatusEffects.FLAME_EMBODIMENT),
    FROST("frost", 0xFF55FFFF, Formatting.AQUA,true, Blocks.SNOW, Blocks.ICE, Blocks.PACKED_ICE, Blocks.WATER, SoundEvents.BLOCK_SNOW_PLACE, SoundEvents.BLOCK_SNOW_BREAK, ModSounds.FROST_CORE_COMPLETION, ModStatusEffects.FROST_EMBODIMENT),
    SOUL("soul", 0xFFAA0000, Formatting.DARK_RED, ModSounds.VITAL_ENERGY_EXTRACTING, ModSounds.VITAL_ENERGY_ABSORBING, ModSounds.SOUL_CORE_COMPLETION, ModStatusEffects.VITAL_ENERGY_OVERFLOW),
    AMORPHOUS("amorphous", 0xFFFF55FF, Formatting.LIGHT_PURPLE, null, null, null, ModStatusEffects.AMORPHOUSNESS),

    PLANT("plant", 0xFF125000, Formatting.DARK_GREEN, null, null, null, null),
    SOIL("soil", 0xFFFFAA00, Formatting.GOLD, null, null, null, null),
    ETHER("ether", 0xFFAA00AA, Formatting.DARK_PURPLE, null, null, null, null),
    ANDROGYNOUS("androgynous", 0xFFFFFFFF, Formatting.OBFUSCATED, null, null, null, null);

    public final int color;
    public final Formatting formatting;
    public final boolean territory;
    public final Block convertBlock;
    public final Block primaryBlock;
    public final Block secondaryBlock;
    public final Block fluidBlock;
    public final SoundEvent extractSound;
    public final SoundEvent absorbSound;
    public final SoundEvent completionSound;
    public final StatusEffect statusEffect;
    public final String name;


    Element(String name, int color, Formatting formatting, boolean territory, Block convertBlock, Block primaryBlock, Block secondaryBlock, Block fluidBlock, SoundEvent extractSound, SoundEvent absorbSound, SoundEvent completionSound, StatusEffect statusEffect) {
        this.name = name;
        this.color = color;
        this.formatting = formatting;
        this.territory = territory;
        this.convertBlock = convertBlock;
        this.primaryBlock = primaryBlock;
        this.secondaryBlock = secondaryBlock;
        this.fluidBlock = fluidBlock;
        this.extractSound = extractSound;
        this.absorbSound = absorbSound;
        this.completionSound = completionSound;
        this.statusEffect = statusEffect;
    }
    Element(String name, int color, Formatting formatting, SoundEvent extractSound, SoundEvent absorbSound, SoundEvent completionSound, StatusEffect statusEffect) {
        this(name, color, formatting, false, null, null, null, null, extractSound, absorbSound, completionSound, statusEffect);
    }

    public String getIdentifier() {
        return ElementalSmithing.MOD_ID + ".element." + name;
    }

}
