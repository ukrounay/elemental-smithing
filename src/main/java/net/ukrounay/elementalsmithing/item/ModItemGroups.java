package net.ukrounay.elementalsmithing.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.ModBlocks;

public class ModItemGroups {

    public static final ItemGroup MOD_GLOBAL_ITEM_GROUP = Registry.register(
        Registries.ITEM_GROUP,
        new Identifier(ElementalSmithing.MOD_ID, "global_group"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.global"))
            .icon(() -> new ItemStack(ModItems.STEEL_INGOT)).entries(
                (displayContext, entries) -> {
                    entries.addAll(ModBlocks.ModBlockItemRegistry);
                    entries.addAll(ModItems.ModItemRegistry);
                }
            ).build()
    );

    public static void registerItemGroups() {
        ElementalSmithing.LOGGER.info("Registering Item Groups for " + ElementalSmithing.MOD_ID);
    }
}
