package net.ukrounay.elementalsmithing.structure.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.ukrounay.elementalsmithing.ElementalSmithing;

import java.util.Optional;


public class VillagerSpawnFeature extends Feature<DefaultFeatureConfig> {

    private final VillagerProfession villagerProfession;
    private final Identifier structureId;

    public VillagerSpawnFeature(Codec<DefaultFeatureConfig> codec, VillagerProfession profession, Identifier structureId) {
        super(codec);
        this.villagerProfession = profession;
        this.structureId = structureId;
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        // Load and place the structure
        // Ensure your structure is placed correctly
        StructureWorldAccess world = context.getWorld();
        ChunkGenerator generator = context.getGenerator();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();
        Optional<StructureTemplate> template = world.toServerWorld().getStructureTemplateManager().getTemplate(structureId);

        if (template.isPresent()) {
            StructurePlacementData placementData = new StructurePlacementData()
                    .setRotation(BlockRotation.NONE)
                    .setMirror(BlockMirror.NONE)
                    .setIgnoreEntities(false)
                    .setPosition(null);

            template.get().place(world, pos, pos, placementData, random, Block.NOTIFY_LISTENERS);
            BlockPos spawnPos = pos.add(1, 1, 1);
            ElementalSmithing.LOGGER.info("spawning villager for structure");

            // Add custom villager
            ServerWorld serverWorld = world.toServerWorld();
            VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, serverWorld);
            villager.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5, 0, 0);
            villager.setVillagerData(villager.getVillagerData().withProfession(villagerProfession));
            villager.setCustomName(Text.translatable("villager.elementalsmithing.random_name.{}", random.nextInt(12)));
            serverWorld.spawnEntity(villager);

            return true;
        }

        return false;
    }
}
