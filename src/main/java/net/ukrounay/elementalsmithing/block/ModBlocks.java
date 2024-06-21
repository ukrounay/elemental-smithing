package net.ukrounay.elementalsmithing.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.custom.FusionSmithingTableBlock;
import net.ukrounay.elementalsmithing.item.ModItems;

public class ModBlocks {

    public static final Block STEEL_BLOCK = registerBlock("steel_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK)));

    public static final Block FUSION_SMITHING_TABLE = registerBlock("fusion_smithing_table",
            new FusionSmithingTableBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).nonOpaque()));




    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ElementalSmithing.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        Item item = Registry.register(Registries.ITEM, new Identifier(ElementalSmithing.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        ModItems.ModItemRegistry.add(item.getDefaultStack());
        return item;
    }

    public static void registerModBlocks() {
        ElementalSmithing.LOGGER.info("Registering ModBlocks for " + ElementalSmithing.MOD_ID);
    }
}
