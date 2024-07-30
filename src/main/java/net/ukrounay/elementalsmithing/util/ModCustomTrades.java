package net.ukrounay.elementalsmithing.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.item.ModItems;
import net.ukrounay.elementalsmithing.villager.ModVillagers;

public class ModCustomTrades {
    public static void registerCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.GRAND_BLACKSMITH, 1,
                factories -> {
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.GRAPHITE, 16),
                        new ItemStack(Items.EMERALD, 5),
                        12, 5, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Items.EMERALD, 10),
                        new ItemStack(ModItems.STEEL_SWORD),
                        3, 10, 0.075f));
                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.GRAND_BLACKSMITH, 2,
                factories -> {
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Items.AMETHYST_SHARD, 1),
                        new ItemStack(Items.EMERALD, 1),
                        128, 10, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Items.IRON_INGOT, 3),
                        new ItemStack(Items.EMERALD, 3),
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        128, 15, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                            new ItemStack(Items.EMERALD, 3),
                            new ItemStack(ModItems.STEEL_CHRONICLES_BOOK),
                            1, 0, 0));
                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.GRAND_BLACKSMITH, 3,
                factories -> {
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        new ItemStack(Items.EMERALD, 12),
                        new ItemStack(ModItems.DAMASK_STEEL_SWORD, 1),
                        10, 25, 0.075f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Registries.ITEM.get(new Identifier(ElementalSmithing.MOD_ID, "steel_block")), 1),
                        new ItemStack(ModItems.PURE_CRYSTAL, 1),
                        new ItemStack(ModItems.REINFORCED_STEEL_SWORD, 1),
                        1, 30, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        new ItemStack(Items.EMERALD, 14),
                        new ItemStack(ModItems.KNIGHT_HELMET, 1),
                        3, 20, 0.075f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        new ItemStack(Items.EMERALD, 20),
                        new ItemStack(ModItems.KNIGHT_CHESTPLATE, 1),
                        3, 20, 0.075f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        new ItemStack(Items.EMERALD, 18),
                        new ItemStack(ModItems.KNIGHT_LEGGINGS, 1),
                        3, 20, 0.075f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.STEEL_INGOT, 1),
                        new ItemStack(Items.EMERALD, 12),
                        new ItemStack(ModItems.KNIGHT_BOOTS, 1),
                        3, 20, 0.075f));
                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.GRAND_BLACKSMITH, 4,
                factories -> {
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Registries.ITEM.get(new Identifier(ElementalSmithing.MOD_ID, "steel_block")), 1),
                        new ItemStack(ModItems.PURE_CRYSTAL, 3),
                        new ItemStack(ModItems.REINFORCED_KNIGHT_HELMET, 1),
                        1, 30, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Registries.ITEM.get(new Identifier(ElementalSmithing.MOD_ID, "steel_block")), 1),
                        new ItemStack(ModItems.PURE_CRYSTAL, 7),
                        new ItemStack(ModItems.REINFORCED_KNIGHT_CHESTPLATE, 1),
                        1, 30, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Registries.ITEM.get(new Identifier(ElementalSmithing.MOD_ID, "steel_block")), 1),
                        new ItemStack(ModItems.PURE_CRYSTAL, 6),
                        new ItemStack(ModItems.REINFORCED_KNIGHT_LEGGINGS, 1),
                        1, 30, 0.05f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(Registries.ITEM.get(new Identifier(ElementalSmithing.MOD_ID, "steel_block")), 1),
                        new ItemStack(ModItems.PURE_CRYSTAL, 2),
                        new ItemStack(ModItems.REINFORCED_KNIGHT_BOOTS, 1),
                        1, 30, 0.05f));
                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.GRAND_BLACKSMITH, 5,
                factories -> {
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.FLAME_CORE, 1),
                        new ItemStack(Items.EMERALD, 48),
                        new ItemStack(ModItems.FLAME_SWORD, 1),
                        1, 50, 0.075f));
                    factories.add((entity, random) ->  new TradeOffer(
                        new ItemStack(ModItems.FROST_CORE, 1),
                        new ItemStack(Items.EMERALD, 48),
                        new ItemStack(ModItems.FROST_SWORD, 1),
                        1, 50, 0.075f));
                });
    }
}
