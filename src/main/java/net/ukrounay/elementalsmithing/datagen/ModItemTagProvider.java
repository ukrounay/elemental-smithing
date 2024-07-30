package net.ukrounay.elementalsmithing.datagen;

import dev.architectury.platform.Mod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(ModTags.Items.READABLE_BOOKS).add(
                ModItems.STEEL_CHRONICLES_BOOK
        );

        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS).add(
                ModItems.STEEL_INGOT
        );

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR).add(
                ModItems.KNIGHT_HELMET,
                ModItems.KNIGHT_CHESTPLATE,
                ModItems.KNIGHT_LEGGINGS,
                ModItems.KNIGHT_BOOTS
        );

        getOrCreateTagBuilder(ItemTags.HOES).add(
                ModItems.DAMASK_STEEL_HOE,
                ModItems.STEEL_HOE,
                ModItems.PURE_CRYSTAL_HOE
        );
        getOrCreateTagBuilder(ItemTags.AXES).add(
                ModItems.DAMASK_STEEL_AXE,
                ModItems.STEEL_AXE,
                ModItems.PURE_CRYSTAL_AXE
        );
        getOrCreateTagBuilder(ItemTags.PICKAXES).add(
                ModItems.DAMASK_STEEL_PICKAXE,
                ModItems.STEEL_PICKAXE,
                ModItems.PURE_CRYSTAL_PICKAXE
        );

        getOrCreateTagBuilder(ModTags.Items.SWORDS).add(
                ModItems.DAMASK_STEEL_SWORD,
                ModItems.STEEL_SWORD,
                ModItems.CURSED_CRYSTAL_SWORD,
                ModItems.BLESSED_CRYSTAL_SWORD,
                ModItems.PURE_CRYSTAL_SWORD
        );
        getOrCreateTagBuilder(ModTags.Items.ELEMENTAL_SWORDS).add(
                ModItems.FLAME_SWORD,
                ModItems.FROST_SWORD,
                ModItems.AMORPHOUS_SWORD
        );
        getOrCreateTagBuilder(ModTags.Items.SWORDS).forceAddTag(ModTags.Items.ELEMENTAL_SWORDS);
        getOrCreateTagBuilder(ItemTags.SWORDS).forceAddTag(ModTags.Items.SWORDS);

        getOrCreateTagBuilder(ModTags.Items.ELEMENTAL_CORES).add(
                ModItems.FLAME_CORE,
                ModItems.FROST_CORE,
                ModItems.SOUL_CORE
        );
        getOrCreateTagBuilder(ModTags.Items.ELEMENTAL_ITEMS).forceAddTag(ModTags.Items.ELEMENTAL_CORES);
        getOrCreateTagBuilder(ModTags.Items.ELEMENTAL_ITEMS).forceAddTag(ModTags.Items.ELEMENTAL_SWORDS);


        getOrCreateTagBuilder(ModTags.Items.MUSIC_DISCS).add(
                ModItems.TEGUKJFLK_MUSIC_DISK,
                ModItems.IWSEUVHJK_MUSIC_DISK,
                ModItems.ZSDXGHJMR_MUSIC_DISK,
                ModItems.EZSXDFCGH_MUSIC_DISK,
                ModItems.UBDCWJNK_MUSIC_DISK
        );
        getOrCreateTagBuilder(ItemTags.MUSIC_DISCS).forceAddTag(ModTags.Items.MUSIC_DISCS);

        getOrCreateTagBuilder(ModTags.Items.CRYSTALS).add(
                Items.DIAMOND,
                Items.EMERALD,
                Items.AMETHYST_SHARD,
                Items.QUARTZ,
                Items.LAPIS_LAZULI,
                ModItems.GRAPHITE,
                ModItems.BLESSED_CRYSTAL,
                ModItems.CURSED_CRYSTAL,
                ModItems.PURE_CRYSTAL
        );
    }
}
