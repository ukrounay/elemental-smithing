package net.ukrounay.elementalsmithing;

import net.fabricmc.api.ModInitializer;

import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.block.entity.ModBlockEntities;
import net.ukrounay.elementalsmithing.item.ModItemGroups;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.recipe.ModRecipe;
import net.ukrounay.elementalsmithing.screen.ModScreenHandlers;
import net.ukrounay.elementalsmithing.sound.ModSounds;
import net.ukrounay.elementalsmithing.util.ModLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementalSmithing implements ModInitializer {

	public static final String MOD_ID = "elementalsmithing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		ModLootTableModifiers.modifyLootTables();
		ModSounds.registerSounds();
		ModRecipe.registerRecipes();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModParticles.registerParticles();
	}
}