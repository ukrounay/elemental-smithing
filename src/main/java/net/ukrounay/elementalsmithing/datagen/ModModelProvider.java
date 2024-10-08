package net.ukrounay.elementalsmithing.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.util.ModTags;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.STEEL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GRAPHITE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CURSED_CRYSTAL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLESSED_CRYSTAL_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.PURE_CRYSTAL_BLOCK);

        blockStateModelGenerator.registerSimpleState(ModBlocks.ENERGY_CONDENSATOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {


        // handheld items
        itemModelGenerator.register(ModItems.STEEL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STEEL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STEEL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STEEL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.STEEL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.REINFORCED_STEEL_SWORD, Models.HANDHELD);

        itemModelGenerator.register(ModItems.DAMASK_STEEL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DAMASK_STEEL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DAMASK_STEEL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DAMASK_STEEL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.DAMASK_STEEL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.CURSED_CRYSTAL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CURSED_CRYSTAL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CURSED_CRYSTAL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CURSED_CRYSTAL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.CURSED_CRYSTAL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.PURE_CRYSTAL_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PURE_CRYSTAL_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PURE_CRYSTAL_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PURE_CRYSTAL_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.PURE_CRYSTAL_HOE, Models.HANDHELD);

        itemModelGenerator.register(ModItems.FLAME_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.FROST_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.AMORPHOUS_SWORD, Models.HANDHELD);

        itemModelGenerator.register(ModItems.UNSTABLE_AMORPHOUS_SWORD, Models.HANDHELD_ROD);


        // simple items
        itemModelGenerator.register(ModItems.GRAPHITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.STEEL_GOBBET, Models.GENERATED);
        itemModelGenerator.register(ModItems.STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_STEEL, Models.GENERATED);

        itemModelGenerator.register(ModItems.PURE_CRYSTAL, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLESSED_CRYSTAL, Models.GENERATED);
        itemModelGenerator.register(ModItems.CURSED_CRYSTAL, Models.GENERATED);

        itemModelGenerator.register(ModItems.FLAME_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.FROST_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOUL_CORE, Models.GENERATED);

        itemModelGenerator.register(ModItems.DAMASK_STEEL_UPGRADE, Models.GENERATED);

        itemModelGenerator.register(ModItems.TEGUKJFLK_MUSIC_DISK, Models.GENERATED);
        itemModelGenerator.register(ModItems.IWSEUVHJK_MUSIC_DISK, Models.GENERATED);
        itemModelGenerator.register(ModItems.ZSDXGHJMR_MUSIC_DISK, Models.GENERATED);
        itemModelGenerator.register(ModItems.EZSXDFCGH_MUSIC_DISK, Models.GENERATED);
        itemModelGenerator.register(ModItems.UBDCWJNK_MUSIC_DISK, Models.GENERATED);


        // armor
        itemModelGenerator.registerArmor((ArmorItem) ModItems.KNIGHT_HELMET);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.KNIGHT_CHESTPLATE);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.KNIGHT_LEGGINGS);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.KNIGHT_BOOTS);

        itemModelGenerator.registerArmor((ArmorItem) ModItems.REINFORCED_KNIGHT_HELMET);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.REINFORCED_KNIGHT_CHESTPLATE);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.REINFORCED_KNIGHT_LEGGINGS);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.REINFORCED_KNIGHT_BOOTS);

//        // custom
//        itemModelGenerator.register(ModItems.STEEL_SHIELD, Items.SHIELD, Models.CUBE);

    }
}
