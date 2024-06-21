package net.ukrounay.elementalsmithing.recipe;

import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class ModRecipe {
        public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(ElementalSmithing.MOD_ID, FusionSmithingRecipe.Serializer.ID),
                FusionSmithingRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(ElementalSmithing.MOD_ID, FusionSmithingRecipe.Type.ID),
                FusionSmithingRecipe.Type.INSTANCE);
    }
}
