package net.ukrounay.elementalsmithing.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.item.ModItems;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> STEEL_SMELTABLES = List.of(
        ModItems.RAW_STEEL,
        ModItems.STEEL_AXE,
        ModItems.STEEL_HOE,
        ModItems.STEEL_PICKAXE,
        ModItems.STEEL_SHOVEL,
        ModItems.STEEL_SWORD
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
                ModItems.GRAPHITE, 10.0f,1200, "steel");
        offerBlasting(exporter, STEEL_SMELTABLES, RecipeCategory.MISC,
                ModItems.STEEL_GOBBET, 5,800, "steel");
        offerSmelting(exporter, STEEL_SMELTABLES, RecipeCategory.MISC,
                Items.IRON_NUGGET, 0.2f,1000, "steel");

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.STEEL_INGOT,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.STEEL_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.GRAPHITE,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.GRAPHITE_BLOCK);

        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.BLESSED_CRYSTAL,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLESSED_CRYSTAL_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.CURSED_CRYSTAL,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.CURSED_CRYSTAL_BLOCK);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.PURE_CRYSTAL,
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.PURE_CRYSTAL_BLOCK);

        offerSmithingTemplateCopyingRecipe(exporter, ModItems.DAMASK_STEEL_UPGRADE, ModItems.STEEL_INGOT);

        // steel tools
        offerToolsRecipe(exporter,
                ModItems.STEEL_INGOT, ModItems.STEEL_SWORD, ModItems.STEEL_PICKAXE,
                ModItems.STEEL_AXE, ModItems.STEEL_SHOVEL, ModItems.STEEL_HOE);

        // damask steel tools
        offerDamaskUpgradeRecipe(exporter, Items.IRON_AXE, ModItems.DAMASK_STEEL_AXE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_PICKAXE, ModItems.DAMASK_STEEL_PICKAXE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_SWORD, ModItems.DAMASK_STEEL_SWORD);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_HOE, ModItems.DAMASK_STEEL_HOE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_SHOVEL, ModItems.DAMASK_STEEL_SHOVEL);

        // damask steel armor
        offerDamaskUpgradeRecipe(exporter, Items.IRON_HELMET, ModItems.KNIGHT_HELMET);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_CHESTPLATE, ModItems.KNIGHT_CHESTPLATE);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_LEGGINGS, ModItems.KNIGHT_LEGGINGS);
        offerDamaskUpgradeRecipe(exporter, Items.IRON_BOOTS, ModItems.KNIGHT_BOOTS);

        // crystal tools
        offerToolsRecipe(exporter,
                ModItems.BLESSED_CRYSTAL, ModItems.BLESSED_CRYSTAL_SWORD, ModItems.BLESSED_CRYSTAL_PICKAXE,
                ModItems.BLESSED_CRYSTAL_AXE, ModItems.BLESSED_CRYSTAL_SHOVEL, ModItems.BLESSED_CRYSTAL_HOE);
        offerToolsRecipe(exporter,
                ModItems.CURSED_CRYSTAL, ModItems.CURSED_CRYSTAL_SWORD, ModItems.CURSED_CRYSTAL_PICKAXE,
                ModItems.CURSED_CRYSTAL_AXE, ModItems.CURSED_CRYSTAL_SHOVEL, ModItems.CURSED_CRYSTAL_HOE);
        offerToolsRecipe(exporter,
                ModItems.PURE_CRYSTAL, ModItems.PURE_CRYSTAL_SWORD, ModItems.PURE_CRYSTAL_PICKAXE,
                ModItems.PURE_CRYSTAL_AXE, ModItems.PURE_CRYSTAL_SHOVEL, ModItems.PURE_CRYSTAL_HOE);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.STEEL_CHRONICLES_BOOK, 1)
                .input(Items.WRITABLE_BOOK)
                .input(ModItems.STEEL_INGOT)
                .criterion(RecipeProvider.hasItem(ModItems.STEEL_INGOT), RecipeProvider.conditionsFromItem(ModItems.STEEL_INGOT))
                .offerTo(exporter);
    }
    public static void offerDamaskUpgradeRecipe(Consumer<RecipeJsonProvider> exporter, Item base, Item result) {
        offerSmithingUpgradeRecipe(exporter, RecipeCategory.COMBAT, ModItems.DAMASK_STEEL_UPGRADE, base, ModItems.STEEL_INGOT, result);
    }
    public static void offerSmithingUpgradeRecipe(Consumer<RecipeJsonProvider> exporter, RecipeCategory category, Item template, Item base, Item addition, Item result) {
        SmithingTransformRecipeJsonBuilder.create(Ingredient.ofItems(template), Ingredient.ofItems(base), Ingredient.ofItems(addition), category, result).criterion("has_" + addition.getTranslationKey(), RecipeProvider.conditionsFromItem(addition)).offerTo(exporter, RecipeProvider.getItemPath(result) + "_smithing");
    }

    private enum ToolType {
        SWORD("I","I","S"),
        PICKAXE("III"," S "," S "),
        AXE("II","IS"," S"),
        SHOVEL("I","S","S"),
        HOE("II"," S"," S");

        public final Character StickCharacter = 'S';
        public final Character IngredientCharacter = 'I';
        public final String topString;
        public final String midString;
        public final String botString;

        ToolType(String s, String s1, String s2) {
            this.topString = s;
            this.midString = s1;
            this.botString = s2;
        }
    }
    public static void offerToolRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Item tool, ToolType type) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, tool, 1)
                .input(type.StickCharacter, Items.STICK)
                .input(type.IngredientCharacter, ingredient)
                .pattern(type.topString)
                .pattern(type.midString)
                .pattern(type.botString)
                .criterion(RecipeProvider.hasItem(ingredient), RecipeProvider.conditionsFromItem(ingredient))
                .offerTo(exporter);
    }
    public static void offerSwordRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Optional<Item> sword) {
        sword.ifPresent(item -> offerToolRecipe(exporter, ingredient, item, ToolType.SWORD));
    }
    public static void offerPickaxeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Optional<Item> pickaxe) {
        pickaxe.ifPresent(item -> offerToolRecipe(exporter, ingredient, item, ToolType.PICKAXE));
    }
    public static void offerAxeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Optional<Item> axe) {
        axe.ifPresent(item -> offerToolRecipe(exporter, ingredient, item, ToolType.AXE));
    }
    public static void offerShovelRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Optional<Item> shovel) {
        shovel.ifPresent(item -> offerToolRecipe(exporter, ingredient, item, ToolType.SHOVEL));
    }
    public static void offerHoeRecipe(Consumer<RecipeJsonProvider> exporter, Item ingredient, Optional<Item> hoe) {
        hoe.ifPresent(item -> offerToolRecipe(exporter, ingredient, item, ToolType.HOE));
    }
    public static void offerToolsRecipe(
            Consumer<RecipeJsonProvider> exporter, Item ingredient,
            Item sword, Item pickaxe, Item axe, Item shovel, Item hoe
    ) {
        offerSwordRecipe(exporter, ingredient, sword == null ? Optional.empty() : Optional.of(sword));
        offerPickaxeRecipe(exporter, ingredient, pickaxe == null ? Optional.empty() : Optional.of(pickaxe));
        offerAxeRecipe(exporter, ingredient, axe == null ? Optional.empty() : Optional.of(axe));
        offerShovelRecipe(exporter, ingredient, shovel == null ? Optional.empty() : Optional.of(shovel));
        offerHoeRecipe(exporter, ingredient, hoe == null ? Optional.empty() : Optional.of(hoe));
    }
}
