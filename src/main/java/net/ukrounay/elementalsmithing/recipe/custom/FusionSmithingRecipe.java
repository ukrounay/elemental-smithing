package net.ukrounay.elementalsmithing.recipe.custom;

import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity;


import java.util.List;

public class FusionSmithingRecipe implements Recipe<SimpleInventory> {
    private final Identifier identifier;
    private final Ingredient leftFusionItem;
    private final Ingredient rightFusionItem;
    private final List<Ingredient> craftItems;
    private final ItemStack output;

    public FusionSmithingRecipe(Identifier identifier, ItemStack output, List<Ingredient> craftItems, Ingredient leftFusionItem, Ingredient rightFusionItem) {
        this.identifier = identifier;
        this.output = output;
        this.craftItems = craftItems;
        this.leftFusionItem = leftFusionItem;
        this.rightFusionItem = rightFusionItem;
    }


    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) return false;
        return
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_1).isDamaged() && craftItems.get(0).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_1)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_2).isDamaged() && craftItems.get(1).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_2)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_3).isDamaged() && craftItems.get(2).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_3)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_4).isDamaged() && craftItems.get(3).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_4)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_5).isDamaged() && craftItems.get(4).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_5)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_6).isDamaged() && craftItems.get(5).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_6)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_7).isDamaged() && craftItems.get(6).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_7)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_8).isDamaged() && craftItems.get(7).test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_CRAFT_8)) &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_LEFT).isDamaged() &&
            !inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_RIGHT).isDamaged()
                    && (leftFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_LEFT))
                    || rightFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_LEFT)))
                    && (leftFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_RIGHT))
                    || rightFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_RIGHT)));

        // shapeless recipe -->

//        if(world.isClient()) return false;
//        RecipeMatcher recipeMatcher = new RecipeMatcher();
////        ElementalSmithing.LOGGER.info("checking recipe");
//        int i = 0;
//        for (int j = FusionSmithingTableBlockEntity.FUSION_CRAFT_1; j <= FusionSmithingTableBlockEntity.FUSION_CRAFT_8; ++j) {
//            ItemStack itemStack = inventory.getStack(j);
//            if (itemStack.isEmpty()) continue;
//            ++i;
//            recipeMatcher.addInput(itemStack, 1);
//        }
//        return i == this.craftItems.size() &&
//            recipeMatcher.match(this, null) &&
//            leftFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_LEFT)) &&
//            rightFusionItem.test(inventory.getStack(FusionSmithingTableBlockEntity.FUSION_INGREDIENT_RIGHT));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = this.output.copy();
        NbtCompound nbtCompound = inventory.getStack(FusionSmithingTableBlockEntity.FUSION_OUTPUT).getNbt();
        if (nbtCompound != null) {
            itemStack.setNbt(nbtCompound.copy());
        }
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> out = DefaultedList.of();
        out.add(leftFusionItem);
        out.add(rightFusionItem);
        out.add(craftItems.get(0));
        out.add(craftItems.get(1));
        out.add(craftItems.get(2));
        out.add(craftItems.get(3));
        out.add(craftItems.get(4));
        out.add(craftItems.get(5));
        out.add(craftItems.get(6));
        out.add(craftItems.get(7));
        return out;
    }

    @Override
    public Identifier getId() {
        return identifier;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }



    public static class Type implements RecipeType<FusionSmithingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fusion";
    }


    public static class Serializer implements RecipeSerializer<FusionSmithingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "fusion";

        @Override
        public FusionSmithingRecipe read(Identifier identifier, JsonObject jsonObject) {

            DefaultedList<Ingredient> craftingStacks = DefaultedList.of();
            for (int i = 0; i < 9; ++i)
                craftingStacks.add(
                    JsonHelper.hasElement(jsonObject, "fusing_craft_" + i)
                        ? Ingredient.fromJson(JsonHelper.getElement(jsonObject, "fusing_craft_" + i),true)
                        : Ingredient.empty()
                );

            Ingredient leftItem = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "fusing_left"));
            Ingredient rightItem = Ingredient.fromJson(JsonHelper.getElement(jsonObject, "fusing_right"));
            ItemStack outputStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

            return new FusionSmithingRecipe(identifier, outputStack, craftingStacks, leftItem, rightItem);
        }


        @Override
        public FusionSmithingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> craftingStacks = DefaultedList.ofSize(i, Ingredient.EMPTY);
            for (int j = 0; j < craftingStacks.size(); ++j) {
                craftingStacks.set(j, Ingredient.fromPacket(packetByteBuf));
            }
            Ingredient leftItem = Ingredient.fromPacket(packetByteBuf);
            Ingredient rightItem = Ingredient.fromPacket(packetByteBuf);
            ItemStack outputStack = packetByteBuf.readItemStack();
            return new FusionSmithingRecipe(identifier, outputStack, craftingStacks, leftItem, rightItem);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, FusionSmithingRecipe recipe) {
            packetByteBuf.writeVarInt(recipe.craftItems.size());
            for (Ingredient ingredient : recipe.craftItems)
                ingredient.write(packetByteBuf);
            recipe.leftFusionItem.write(packetByteBuf);
            recipe.rightFusionItem.write(packetByteBuf);
            packetByteBuf.writeItemStack(recipe.output);
        }

    }
}
