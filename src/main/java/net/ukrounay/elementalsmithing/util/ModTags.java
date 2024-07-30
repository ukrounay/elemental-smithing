package net.ukrounay.elementalsmithing.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class ModTags {


    public static class Blocks {

        public static final TagKey<Block> ENERGY_PROVIDER = createTag("energy_providers");
        public static final TagKey<Block> ENERGY_BARRIER = createTag("energy_barriers");
        public static final TagKey<Block> CONDENSED_ENERGY_PROVIDER = createTag("condensed_energy_providers");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(ElementalSmithing.MOD_ID, name));
        }
    }
    public static class Items {

        public static final TagKey<Item> MUSIC_DISCS = createTag("music_discs");
        public static final TagKey<Item> CRYSTALS = createTag("crystals");
        public static final TagKey<Item> SWORDS = createTag("swords");
        public static final TagKey<Item> ELEMENTAL_ITEMS = createTag("elemental_items");
        public static final TagKey<Item> ELEMENTAL_CORES = createTag("elemental_cores");
        public static final TagKey<Item> ELEMENTAL_SWORDS = createTag("elemental_swords");
        public static final TagKey<Item> READABLE_BOOKS = createTag("readable_books");


        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(ElementalSmithing.MOD_ID, name));
        }
    }
}
