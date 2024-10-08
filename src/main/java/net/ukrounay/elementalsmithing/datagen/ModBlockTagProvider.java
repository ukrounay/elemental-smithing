package net.ukrounay.elementalsmithing.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.STEEL_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.GRAPHITE_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.BLESSED_CRYSTAL_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.CURSED_CRYSTAL_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.PURE_CRYSTAL_BLOCK);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.FUSION_SMITHING_TABLE);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(ModBlocks.ENERGY_CONDENSATOR);


        getOrCreateTagBuilder(ModTags.Blocks.ENERGY_PROVIDER).add(ModBlocks.BLESSED_CRYSTAL_BLOCK);
        getOrCreateTagBuilder(ModTags.Blocks.ENERGY_PROVIDER).add(ModBlocks.CURSED_CRYSTAL_BLOCK);
        getOrCreateTagBuilder(ModTags.Blocks.ENERGY_PROVIDER).add(ModBlocks.PURE_CRYSTAL_BLOCK);

        getOrCreateTagBuilder(ModTags.Blocks.ENERGY_BARRIER).forceAddTag(BlockTags.WITHER_IMMUNE);
        getOrCreateTagBuilder(ModTags.Blocks.ENERGY_BARRIER).add(ModBlocks.STEEL_BLOCK);

        getOrCreateTagBuilder(ModTags.Blocks.CONDENSED_ENERGY_PROVIDER).add(ModBlocks.ENERGY_CONDENSATOR);
    }
}
