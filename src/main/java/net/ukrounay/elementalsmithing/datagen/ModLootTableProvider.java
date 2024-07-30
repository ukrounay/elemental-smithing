package net.ukrounay.elementalsmithing.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.ukrounay.elementalsmithing.block.ModBlocks;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.STEEL_BLOCK);
        addDrop(ModBlocks.GRAPHITE_BLOCK);
        addDrop(ModBlocks.BLESSED_CRYSTAL_BLOCK);
        addDrop(ModBlocks.CURSED_CRYSTAL_BLOCK);
        addDrop(ModBlocks.PURE_CRYSTAL_BLOCK);
        addDrop(ModBlocks.FUSION_SMITHING_TABLE);
        addDrop(ModBlocks.ENERGY_CONDENSATOR);

    }
}
