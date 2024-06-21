package net.ukrounay.elementalsmithing.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.Recipe;
import net.ukrounay.elementalsmithing.recipe.FusionSmithingRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FusionSmithingDisplay extends BasicDisplay {
    public FusionSmithingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public FusionSmithingDisplay(FusionSmithingRecipe recipe) {
        super(getInputList(recipe), List.of(EntryIngredient.of(EntryStacks.of(recipe.getOutput(null)))));
    }

    private static List<EntryIngredient> getInputList(FusionSmithingRecipe recipe) {
        if(recipe == null) return Collections.emptyList();
        List<EntryIngredient> list = new ArrayList<>();
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(0)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(1)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(2)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(3)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(4)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(5)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(6)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(7)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(8)));
        list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(9)));
        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FusionSmithingCategory.FUSION_SMITHING;
    }


}
