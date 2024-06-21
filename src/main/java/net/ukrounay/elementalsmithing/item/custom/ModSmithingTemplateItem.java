package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.ukrounay.elementalsmithing.ElementalSmithing;

import java.util.List;

public class ModSmithingTemplateItem extends SmithingTemplateItem {

    public enum Type {
        UPGRADE, CORE, FUSION_CORE
    }

    private static final Identifier EMPTY_ARMOR_SLOT_HELMET_TEXTURE = new Identifier("item/empty_armor_slot_helmet");
    private static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE = new Identifier("item/empty_armor_slot_chestplate");
    private static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE = new Identifier("item/empty_armor_slot_leggings");
    private static final Identifier EMPTY_ARMOR_SLOT_BOOTS_TEXTURE = new Identifier("item/empty_armor_slot_boots");
    private static final Identifier EMPTY_SLOT_HOE_TEXTURE = new Identifier("item/empty_slot_hoe");
    private static final Identifier EMPTY_SLOT_AXE_TEXTURE = new Identifier("item/empty_slot_axe");
    private static final Identifier EMPTY_SLOT_SWORD_TEXTURE = new Identifier("item/empty_slot_sword");
    private static final Identifier EMPTY_SLOT_SHOVEL_TEXTURE = new Identifier("item/empty_slot_shovel");
    private static final Identifier EMPTY_SLOT_PICKAXE_TEXTURE = new Identifier("item/empty_slot_pickaxe");
    private static final Identifier EMPTY_SLOT_INGOT_TEXTURE = new Identifier("item/empty_slot_ingot");

    private static List<Identifier> getModEmptyBaseSlotTextures(Type type) {
        return switch (type) {
            case FUSION_CORE -> List.of(EMPTY_SLOT_SWORD_TEXTURE);
            default -> List.of(EMPTY_SLOT_SWORD_TEXTURE, EMPTY_ARMOR_SLOT_HELMET_TEXTURE, EMPTY_ARMOR_SLOT_CHESTPLATE_TEXTURE, EMPTY_SLOT_PICKAXE_TEXTURE, EMPTY_ARMOR_SLOT_LEGGINGS_TEXTURE, EMPTY_SLOT_AXE_TEXTURE, EMPTY_ARMOR_SLOT_BOOTS_TEXTURE, EMPTY_SLOT_HOE_TEXTURE, EMPTY_SLOT_SHOVEL_TEXTURE);
        };
    }

    private static List<Identifier> getModEmptyAdditionsSlotTextures(Type type) {
        return switch (type) {
            case FUSION_CORE -> List.of(EMPTY_SLOT_SWORD_TEXTURE);
            default -> List.of(EMPTY_SLOT_INGOT_TEXTURE);
        };
    }

    public ModSmithingTemplateItem(String name, Type type, Formatting formatting) {
        super(
            Text.translatable(Util.createTranslationKey("item", new Identifier(ElementalSmithing.MOD_ID, "smithing_template." + name + ".applies_to"))),
            Text.translatable(Util.createTranslationKey("item", new Identifier(ElementalSmithing.MOD_ID, "smithing_template." + name + ".ingredients"))),
            Text.translatable(Util.createTranslationKey("item", new Identifier(ElementalSmithing.MOD_ID, "smithing_template." + name + ".title"))).formatted(formatting),
            Text.translatable(Util.createTranslationKey("item", new Identifier(ElementalSmithing.MOD_ID, "smithing_template." + name + ".base_slot_description"))),
            Text.translatable(Util.createTranslationKey("item", new Identifier(ElementalSmithing.MOD_ID, "smithing_template." + name + ".additions_slot_description"))),
            getModEmptyBaseSlotTextures(type),
            getModEmptyAdditionsSlotTextures(type)
        );
    }
}
