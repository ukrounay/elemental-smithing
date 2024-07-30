package net.ukrounay.elementalsmithing.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.recipe.custom.FusionSmithingRecipe;
import net.ukrounay.elementalsmithing.screen.custom.FusionSmithingScreenHandler;

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

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        SimpleTransferHandler handler = SimpleTransferHandler.create(
                FusionSmithingScreenHandler.class, // The class of your crafting menu
                FusionSmithingCategory.FUSION_SMITHING, // The category identifier of your display
                new SimpleTransferHandler.IntRange(0, 10) // The range of recipe slots
        );
        registry.register(handler);
    }
}
