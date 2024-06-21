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

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(ElementalSmithing.MOD_ID, name));
        }
    }
    public static class Items {

        public static final TagKey<Item> ADVANSED_UPGRADE_TEMPLATES = createTag("advanced_upgrade_templates");
        public static final TagKey<Item> MUSIC_DISCS = createTag("music_discs");
        public static final TagKey<Item> CRYSTALS = createTag("crystals");


        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(ElementalSmithing.MOD_ID, name));
        }
    }
}
