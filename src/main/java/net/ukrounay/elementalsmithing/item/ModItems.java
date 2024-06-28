package net.ukrounay.elementalsmithing.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.item.custom.*;
import net.ukrounay.elementalsmithing.sound.ModSounds;

import java.util.ArrayList;
import java.util.Collection;

public class ModItems {

    // item registry
    public static Collection<ItemStack> ModItemRegistry = new ArrayList<>();


    // steel ingredients
    public static final Item GRAPHITE = registerItem("graphite", new Item(new FabricItemSettings()));
    public static final Item RAW_STEEL = registerItem("raw_steel", new Item(new FabricItemSettings()));
    public static final Item STEEL_GOBBET = registerItem("steel_gobbet", new Item(new FabricItemSettings()));
    public static final Item STEEL_INGOT = registerItem("steel_ingot", new Item(new FabricItemSettings()));


    // steel tools
    public static final Item STEEL_SWORD = registerItem("steel_sword",
            new SwordItem(ModToolMaterial.STEEL,3, -2.4f, new FabricItemSettings()));
    public static final Item STEEL_SHOVEL = registerItem("steel_shovel",
            new ShovelItem(ModToolMaterial.STEEL,1.5f, -3f, new FabricItemSettings()));
    public static final Item STEEL_PICKAXE = registerItem("steel_pickaxe",
            new PickaxeItem(ModToolMaterial.STEEL,1, -2.8f, new FabricItemSettings()));
    public static final Item STEEL_AXE = registerItem("steel_axe",
            new AxeItem(ModToolMaterial.STEEL,5.0f, -3.0f, new FabricItemSettings()));
    public static final Item STEEL_HOE = registerItem("steel_hoe",
            new HoeItem(ModToolMaterial.STEEL,-2, 0.0f, new FabricItemSettings()));


    // damask steel equipment
    public static final Item DAMASK_STEEL_UPGRADE = registerItem("damask_steel_upgrade",
            new ModSmithingTemplateItem("damask_steel_upgrade", ModSmithingTemplateItem.Type.UPGRADE, Formatting.DARK_GRAY));

    public static final Item DAMASK_STEEL_SWORD = registerItem("damask_steel_sword",
            new SwordItem(ModToolMaterial.DAMASK_STEEL,3, -2.3f, new FabricItemSettings()));
    public static final Item DAMASK_STEEL_SHOVEL = registerItem("damask_steel_shovel",
            new ShovelItem(ModToolMaterial.STEEL,1.5f, -2.9f, new FabricItemSettings()));
    public static final Item DAMASK_STEEL_PICKAXE = registerItem("damask_steel_pickaxe",
            new PickaxeItem(ModToolMaterial.DAMASK_STEEL,1, -2.7f, new FabricItemSettings()));
    public static final Item DAMASK_STEEL_AXE = registerItem("damask_steel_axe",
            new AxeItem(ModToolMaterial.DAMASK_STEEL,5.0f, -2.9f, new FabricItemSettings()));
    public static final Item DAMASK_STEEL_HOE = registerItem("damask_steel_hoe",
            new HoeItem(ModToolMaterial.DAMASK_STEEL,-3, 0.1f, new FabricItemSettings()));

    public static final Item REINFORCED_STEEL_SWORD = registerItem("reinforced_steel_sword",
            new SwordItem(ModToolMaterial.STEEL,5, -2.7f, new FabricItemSettings().fireproof().rarity(Rarity.UNCOMMON)));

    public static final Item KNIGHT_HELMET = registerItem("knight_helmet",
            new ArmorItem(ModArmorMaterials.DAMASK_STEEL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item KNIGHT_CHESTPLATE = registerItem("knight_chestplate",
            new ModArmorItem(ModArmorMaterials.DAMASK_STEEL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item KNIGHT_LEGGINGS = registerItem("knight_leggings",
            new ArmorItem(ModArmorMaterials.DAMASK_STEEL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item KNIGHT_BOOTS = registerItem("knight_boots",
            new ArmorItem(ModArmorMaterials.DAMASK_STEEL, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    public static final Item REINFORCED_KNIGHT_HELMET = registerItem("reinforced_knight_helmet",
            new ArmorItem(ModArmorMaterials.REINFORCED_DAMASK_STEEL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item REINFORCED_KNIGHT_CHESTPLATE = registerItem("reinforced_knight_chestplate",
            new ModArmorItem(ModArmorMaterials.REINFORCED_DAMASK_STEEL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item REINFORCED_KNIGHT_LEGGINGS = registerItem("reinforced_knight_leggings",
            new ArmorItem(ModArmorMaterials.REINFORCED_DAMASK_STEEL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item REINFORCED_KNIGHT_BOOTS = registerItem("reinforced_knight_boots",
            new ArmorItem(ModArmorMaterials.REINFORCED_DAMASK_STEEL, ArmorItem.Type.BOOTS, new FabricItemSettings()));


    // crystals
    public static final Item BLESSED_CRYSTAL = registerItem("blessed_crystal", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item CURSED_CRYSTAL = registerItem("cursed_crystal", new Item(new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item PURE_CRYSTAL = registerItem("pure_crystal", new Item(new FabricItemSettings().rarity(Rarity.RARE)));


    // crystal tools
    public static final Item BLESSED_CRYSTAL_SWORD = registerItem("blessed_crystal_sword",
            new SwordItem(ModToolMaterial.BLESSED_CRYSTAL,3, -2.4f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item BLESSED_CRYSTAL_SHOVEL = registerItem("blessed_crystal_shovel",
            new ShovelItem(ModToolMaterial.BLESSED_CRYSTAL,1.5f, -3f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item BLESSED_CRYSTAL_PICKAXE = registerItem("blessed_crystal_pickaxe",
            new PickaxeItem(ModToolMaterial.BLESSED_CRYSTAL,1, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item BLESSED_CRYSTAL_AXE = registerItem("blessed_crystal_axe",
            new AxeItem(ModToolMaterial.BLESSED_CRYSTAL,5.0f, -3.0f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item BLESSED_CRYSTAL_HOE = registerItem("blessed_crystal_hoe",
            new HoeItem(ModToolMaterial.BLESSED_CRYSTAL,-2, 0.0f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));

    public static final Item CURSED_CRYSTAL_SWORD = registerItem("cursed_crystal_sword",
            new SwordItem(ModToolMaterial.CURSED_CRYSTAL,3, -2.4f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item CURSED_CRYSTAL_SHOVEL = registerItem("cursed_crystal_shovel",
            new ShovelItem(ModToolMaterial.CURSED_CRYSTAL,1.5f, -3f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item CURSED_CRYSTAL_PICKAXE = registerItem("cursed_crystal_pickaxe",
            new PickaxeItem(ModToolMaterial.CURSED_CRYSTAL,1, -2.8f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item CURSED_CRYSTAL_AXE = registerItem("cursed_crystal_axe",
            new AxeItem(ModToolMaterial.CURSED_CRYSTAL,5.0f, -3.0f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item CURSED_CRYSTAL_HOE = registerItem("cursed_crystal_hoe",
            new HoeItem(ModToolMaterial.CURSED_CRYSTAL,-2, 0.0f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));



    public static final Item PURE_CRYSTAL_SWORD = registerItem("pure_crystal_sword",
            new SwordItem(ModToolMaterial.PURE_CRYSTAL,3, -2.4f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item PURE_CRYSTAL_SHOVEL = registerItem("pure_crystal_shovel",
            new ShovelItem(ModToolMaterial.PURE_CRYSTAL,1.5f, -3f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item PURE_CRYSTAL_PICKAXE = registerItem("pure_crystal_pickaxe",
            new PickaxeItem(ModToolMaterial.PURE_CRYSTAL,1, -2.8f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item PURE_CRYSTAL_AXE = registerItem("pure_crystal_axe",
            new AxeItem(ModToolMaterial.PURE_CRYSTAL,5.0f, -3.0f, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item PURE_CRYSTAL_HOE = registerItem("pure_crystal_hoe",
            new HoeItem(ModToolMaterial.PURE_CRYSTAL,-2, 0.0f, new FabricItemSettings().rarity(Rarity.RARE)));

    // elemental items
    public static final Item FLAME_CORE = registerItem("flame_core",
            new FlameElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(1000).fireproof()), true);
    public static final Item FROST_CORE = registerItem("frost_core",
            new FrostElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(1000)), true);
    public static final Item SOUL_CORE = registerItem("soul_core",
            new SoulElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(1000)), true);

//    public static final ItemStack FLAME_CORE_DAMAGED = registerDamagedItem("flame_core",
//            new FlameElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(100).fireproof()));
//    public static final ItemStack FROST_CORE_DAMAGED = registerDamagedItem("frost_core",
//            new FrostElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(100)));
//    public static final ItemStack HARMONY_CORE_DAMAGED = registerDamagedItem("harmony_core",
//            new SoulElementalCoreItem(new FabricItemSettings().maxCount(1).maxDamage(100)));

    public static final Item FLAME_SWORD = registerItem("flame_sword",
            new ElementalSwordItem(Element.FLAME,2, -1.8f,
                    new FabricItemSettings().fireproof().rarity(Rarity.RARE), Enchantments.FIRE_ASPECT), true);
    public static final Item FROST_SWORD = registerItem("frost_sword",
            new ElementalSwordItem(Element.FROST, 2, -1.8f,
                    new FabricItemSettings().rarity(Rarity.RARE), Enchantments.UNBREAKING), true);
    public static final Item AMORPHOUS_SWORD = registerItem("amorphous_sword",
            new ElementalSwordItem(Element.AMORPHOUS, 8, -1.4f,
                    new FabricItemSettings().rarity(Rarity.EPIC), Enchantments.MENDING), true);

    // miscellaneous
    public static final Item TEGUKJFLK_MUSIC_DISK = registerItem("tegukjflk_music_disk",
            new MusicDiscItem(0, ModSounds.TEGUKJFLK, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE), 59));

    public static final Item IWSEUVHJK_MUSIC_DISK = registerItem("iwseuvhjk_music_disk",
            new MusicDiscItem(1, ModSounds.IWSEUVHJK, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE), 75));


    private static Item registerItem(String name, Item item) {
        ModItemRegistry.add(item.getDefaultStack());
        return Registry.register(Registries.ITEM, new Identifier(ElementalSmithing.MOD_ID, name), item);
    }

    private static Item registerItem(String name, Item item, boolean addDamaged) {
        if (addDamaged && item.isDamageable()) {
            var stack = item.getDefaultStack();
            stack.setDamage(item.getMaxDamage());
            ModItemRegistry.add(stack);
        }
        return registerItem(name, item);
    }
//    private static Item registerItem(String name, Item item, Enchantment infusedEnchantment, boolean addDamaged) {
//        if (infusedEnchantment != null) {
//            if (addDamaged && item.isDamageable()) {
//                var stack = item.getDefaultStack();
//                stack.setDamage(item.getMaxDamage());
//                ModItemRegistry.add(stack);
//            }
//        } else return registerItem(name, item, addDamaged);
//    }
    public static void registerModItems() {
        ElementalSmithing.LOGGER.info("Registering mod items for " + ElementalSmithing.MOD_ID);
    }
}




//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToCombatItemGroup);
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsItemGroup);


//    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries) {
//        entries.add(STEEL_PICKAXE);
//        entries.add(STEEL_AXE);
//        entries.add(STEEL_SHOVEL);
//        entries.add(STEEL_HOE);
//    }
//
//    private static void addItemsToCombatItemGroup(FabricItemGroupEntries entries) {
//        entries.add(STEEL_SWORD);
//        entries.add(KNIGHT_HELMET);
//        entries.add(KNIGHT_CHESTPLATE);
//        entries.add(KNIGHT_LEGGINGS);
//        entries.add(KNIGHT_BOOTS);
//        entries.add(DAMASK_STEEL_SWORD);
//        entries.add(CRYSTAL_CERAMIC_SWORD);
//        entries.add(AMORPHOUS_SWORD);
//    }
//
//    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
//        entries.add(RAW_STEEL);
//        entries.add(STEEL_GOBBET);
//        entries.add(STEEL_INGOT);
//
//        entries.add(CERAMIC_DUST);
//
//        entries.add(FLAME_SWORD);
//        entries.add(FROST_SWORD);
//    }