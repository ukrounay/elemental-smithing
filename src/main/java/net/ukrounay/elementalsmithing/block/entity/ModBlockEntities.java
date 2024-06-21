package net.ukrounay.elementalsmithing.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<FusionSmithingTableBlockEntity> FUSION_SMITHING_TABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(ElementalSmithing.MOD_ID, "fusion_smithing_be"),
                    FabricBlockEntityTypeBuilder.create(FusionSmithingTableBlockEntity::new, ModBlocks.FUSION_SMITHING_TABLE).build());

    public static void registerBlockEntities() {
        ElementalSmithing.LOGGER.info("Registering block entities for" + ElementalSmithing.MOD_ID);
    }

}
