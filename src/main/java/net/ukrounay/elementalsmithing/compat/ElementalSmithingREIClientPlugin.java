package net.ukrounay.elementalsmithing.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.recipe.FusionSmithingRecipe;
import net.ukrounay.elementalsmithing.screen.FusionSmithingScreen;

public class ElementalSmithingREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FusionSmithingCategory());

        registry.addWorkstations(FusionSmithingCategory.FUSION_SMITHING, EntryStacks.of(ModBlocks.FUSION_SMITHING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FusionSmithingRecipe.class, FusionSmithingRecipe.Type.INSTANCE, FusionSmithingDisplay::new);
    }
}
