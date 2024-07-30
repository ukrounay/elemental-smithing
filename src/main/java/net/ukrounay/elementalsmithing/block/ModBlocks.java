package net.ukrounay.elementalsmithing.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.custom.EnergyCondensatorBlock;
import net.ukrounay.elementalsmithing.block.custom.FusionSmithingTableBlock;
import net.ukrounay.elementalsmithing.item.ModItems;

import java.util.ArrayList;
import java.util.Collection;

public class ModBlocks {

    // block item registry
    public static Collection<ItemStack> ModBlockItemRegistry = new ArrayList<>();

    // blocks

    public static final Block STEEL_BLOCK = registerBlock("steel_block",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).hardness(6.0f).resistance(10.0f)));
    public static final Block GRAPHITE_BLOCK = registerBlock("graphite_block",
            new Block(FabricBlockSettings.copyOf(Blocks.COAL_BLOCK).hardness(5.0f).resistance(6.0f)));

    public static final Block BLESSED_CRYSTAL_BLOCK = registerBlock("blessed_crystal_block",
            new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).hardness(3.0f).resistance(10.0f).luminance(4).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
    public static final Block CURSED_CRYSTAL_BLOCK = registerBlock("cursed_crystal_block",
            new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).hardness(3.0f).resistance(4.0f).luminance(4).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));
    public static final Block PURE_CRYSTAL_BLOCK = registerBlock("pure_crystal_block",
            new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).hardness(3.0f).resistance(8.0f).luminance(8).nonOpaque().allowsSpawning(Blocks::never).solidBlock(Blocks::never).suffocates(Blocks::never).blockVision(Blocks::never)));

    public static final Block FUSION_SMITHING_TABLE = registerBlock("fusion_smithing_table",
            new FusionSmithingTableBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).hardness(5.0f).resistance(6.0f).nonOpaque()));

    public static final Block ENERGY_CONDENSATOR = registerBlock("energy_condensator",
            new EnergyCondensatorBlock(FabricBlockSettings.copyOf(Blocks.COBBLESTONE).hardness(5.0f).resistance(6.0f).nonOpaque()));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ElementalSmithing.MOD_ID, name), block);
    }


    private static Item registerBlockItem(String name, Block block) {
        Item item = Registry.register(Registries.ITEM, new Identifier(ElementalSmithing.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        ModBlockItemRegistry.add(item.getDefaultStack());
        return item;
    }

    public static void registerModBlocks() {
        ElementalSmithing.LOGGER.info("Registering ModBlocks for " + ElementalSmithing.MOD_ID);
    }
}
