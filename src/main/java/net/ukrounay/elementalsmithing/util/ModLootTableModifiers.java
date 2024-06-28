package net.ukrounay.elementalsmithing.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.item.ModItems;

public class ModLootTableModifiers {

    public static final Identifier VILLAGE_WEAPONSMITH_CHEST_ID = new Identifier("minecraft", "chests/village/village_weaponsmith");
    public static final Identifier VILLAGE_TOOLSMITH_CHEST_ID = new Identifier("minecraft", "chests/village/village_toolsmith");
    public static final Identifier VILLAGE_ARMORER_CHEST_ID = new Identifier("minecraft", "chests/village/village_armorer");

    public static final Identifier IGLOO_CHEST_ID = new Identifier("minecraft", "chests/igloo_chest");
    public static final Identifier ANCIENT_CITY_ICE_BOX_CHEST_ID = new Identifier("minecraft", "chests/ancient_city_ice_box");

    public static final Identifier NETHER_BRIDGE_CHEST_ID = new Identifier("minecraft", "chests/nether_bridge");
    public static final Identifier RUINED_PORTAL_CHEST_ID = new Identifier("minecraft", "chests/ruined_portal");

    public static final Identifier END_CITY_TREASURE_CHEST_ID = new Identifier("minecraft", "chests/end_city_treasure");

    public static final Identifier FROG_ID = new Identifier("minecraft", "entities/frog");
    public static final Identifier SQUID_ID = new Identifier("minecraft", "entities/squid");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if(id == VILLAGE_WEAPONSMITH_CHEST_ID || id == VILLAGE_TOOLSMITH_CHEST_ID || id == VILLAGE_ARMORER_CHEST_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.3f))
                        .with(ItemEntry.builder(ModItems.DAMASK_STEEL_UPGRADE))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(id == IGLOO_CHEST_ID || id == ANCIENT_CITY_ICE_BOX_CHEST_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.6f))
                        .with(ItemEntry.builder(ModItems.FROST_CORE))
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(ModItems.FROST_CORE.getMaxDamage(), ModItems.FROST_CORE.getMaxDamage())).build())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(id == NETHER_BRIDGE_CHEST_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.3f))
                        .with(ItemEntry.builder(ModItems.FLAME_CORE))
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(ModItems.FLAME_CORE.getMaxDamage(), ModItems.FLAME_CORE.getMaxDamage())).build())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(id == RUINED_PORTAL_CHEST_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .with(ItemEntry.builder(ModItems.FLAME_CORE))
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(ModItems.FLAME_CORE.getMaxDamage(), ModItems.FLAME_CORE.getMaxDamage())).build())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if(id == END_CITY_TREASURE_CHEST_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(1f))
                        .with(ItemEntry.builder(ModItems.SOUL_CORE))
                        .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(ModItems.SOUL_CORE.getMaxDamage(), ModItems.SOUL_CORE.getMaxDamage())).build())
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
