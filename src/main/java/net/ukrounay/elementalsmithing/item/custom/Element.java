package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.ukrounay.elementalsmithing.sound.ModSounds;

public enum Element {
    FLAME(0xFFFF5555, Formatting.RED, true, Blocks.FIRE, Blocks.OBSIDIAN, Blocks.MAGMA_BLOCK, Blocks.LAVA, SoundEvents.ITEM_FIRECHARGE_USE, SoundEvents.BLOCK_FIRE_EXTINGUISH, ModSounds.FLAME_CORE_COMPLETION),
    FROST(0xFF55FFFF, Formatting.AQUA,true, Blocks.SNOW, Blocks.ICE, Blocks.PACKED_ICE, Blocks.WATER, SoundEvents.BLOCK_SNOW_PLACE, SoundEvents.BLOCK_SNOW_BREAK, ModSounds.FROST_CORE_COMPLETION),
    SOUL(0xFFFF55FF, Formatting.LIGHT_PURPLE, ModSounds.VITAL_ENERGY_EXTRACTING, ModSounds.VITAL_ENERGY_ABSORBING, ModSounds.SOUL_CORE_COMPLETION),
    AMORPHOUS(0xFF000000, Formatting.BLACK, null, null, null),

    PLANT(0xFF125000, Formatting.DARK_GREEN, null, null, null),
    SOIL(0xFFFFAA00, Formatting.GOLD, null, null, null),
    ETHER(0xFFAA00AA, Formatting.DARK_PURPLE, null, null, null),
    ANDROGYNOUS(0xFFFFFFFF, Formatting.WHITE, null, null, null);

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

    Element(int color, Formatting formatting, boolean territory, Block convertBlock, Block primaryBlock, Block secondaryBlock, Block fluidBlock, SoundEvent extractSound, SoundEvent absorbSound, SoundEvent completionSound) {

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
    }
    Element(int color, Formatting formatting, SoundEvent extractSound, SoundEvent absorbSound, SoundEvent completionSound) {

        this.color = color;
        this.formatting = formatting;
        this.territory = false;
        this.convertBlock = null;
        this.primaryBlock = null;
        this.secondaryBlock = null;
        this.fluidBlock = null;
        this.extractSound = extractSound;
        this.absorbSound = absorbSound;
        this.completionSound = completionSound;
    }
}
