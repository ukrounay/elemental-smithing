package net.ukrounay.elementalsmithing.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> STEEL_SMELTABLES = List.of(
        ModItems.RAW_STEEL,
        ModItems.STEEL_AXE,
        ModItems.STEEL_HOE,
        ModItems.STEEL_PICKAXE,
        ModItems.STEEL_SHOVEL,
        ModItems.STEEL_SWORD,
        ModItems.DAMASK_STEEL_SWORD
    );
    private static final List<ItemConvertible> GRAPHITE_SMELTABLES = List.of(
        Items.COAL,
        Items.CHARCOAL
    );

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        offerBlasting(exporter, GRAPHITE_SMELTABLES, RecipeCategory.MISC,
                ModItems.GRAPHITE, 10.0f,5000, "steel");
        offerBlasting(exporter, STEEL_SMELTABLES, RecipeCategory.MISC,
                ModItems.STEEL_GOBBET, 5,2500, "steel");
        offerSmelting(exporter, STEEL_SMELTABLES, RecipeCategory.MISC,
                Items.IRON_NUGGET, 0.2f,1000, "steel");

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.STEEL_INGOT,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.STEEL_BLOCK);

        offerSmithingTemplateCopyingRecipe(exporter, ModItems.DAMASK_STEEL_UPGRADE, ModItems.STEEL_INGOT);

        offerDamaskUpgradeRecipe(exporter, Items.IRON_AXE, ModItems.DAMASK_STEEL_AXE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_PICKAXE, ModItems.DAMASK_STEEL_PICKAXE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_SWORD, ModItems.DAMASK_STEEL_SWORD);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_HOE, ModItems.DAMASK_STEEL_HOE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_SHOVEL, ModItems.DAMASK_STEEL_SHOVEL);

        offerDamaskUpgradeRecipe(exporter, Items.IRON_HELMET, ModItems.KNIGHT_HELMET);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_CHESTPLATE, ModItems.KNIGHT_CHESTPLATE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_LEGGINGS, ModItems.KNIGHT_LEGGINGS);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_BOOTS, ModItems.KNIGHT_BOOTS);

    }
    public static void offerDamaskUpgradeRecipe(Consumer<RecipeJsonProvider> exporter, Item base, Item result) {
        offerSmithingUpgradeRecipe(exporter, RecipeCategory.COMBAT, ModItems.DAMASK_STEEL_UPGRADE, base, ModItems.STEEL_INGOT, result);
    }
    public static void offerSmithingUpgradeRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, Item template, Item base, Item addition, Item result) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(template), Ingredient.ofItems(base), Ingredient.ofItems(addition), category, result).criterion("has_" + addition.getTranslationKey(), RecipeProvider.conditionsFromItem(addition)).offerTo(exporter, RecipeProvider.getItemPath(result) + "_smithing");
    }

}
