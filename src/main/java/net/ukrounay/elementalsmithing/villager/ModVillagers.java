package net.ukrounay.elementalsmithing.villager;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.VillageGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.ModBlocks;

public class ModVillagers {

    public static final RegistryKey<PointOfInterestType> GRAND_BLACKSMITH_POI_KEY = poiKey("grand_blacksmith_poi");
    public static final PointOfInterestType GRAND_BLACKSMITH_POI = registerPoi("grand_blacksmith_poi", ModBlocks.FUSION_SMITHING_TABLE);

    public static final VillagerProfession GRAND_BLACKSMITH = registerProfession("grand_blacksmith", GRAND_BLACKSMITH_POI_KEY);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(ElementalSmithing.MOD_ID, name),
                new VillagerProfession(name, entry -> entry.matchesKey(type), entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH));
    }

    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(ElementalSmithing.MOD_ID, name), 1,1, block);
    }

    private static RegistryKey<PointOfInterestType> poiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(ElementalSmithing.MOD_ID, name));
    }

    public static void registerVillagers() {
        ElementalSmithing.LOGGER.info("Registering villagers for " + ElementalSmithing.MOD_ID);
    }
}
