package net.ukrounay.elementalsmithing;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.ukrounay.elementalsmithing.block.ModBlocks;
import net.ukrounay.elementalsmithing.block.entity.ModBlockEntities;
import net.ukrounay.elementalsmithing.block.entity.renderer.EnergyCondensatorBlockEntityRenderer;
import net.ukrounay.elementalsmithing.particles.custom.EnergyParticle;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.particles.custom.TracingEnergyParticle;
import net.ukrounay.elementalsmithing.screen.custom.FusionSmithingScreen;
import net.ukrounay.elementalsmithing.screen.ModScreenHandlers;

public class ElementalSmithingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CURSED_CRYSTAL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLESSED_CRYSTAL_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PURE_CRYSTAL_BLOCK, RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.register(ModBlockEntities.ENERGY_CONDENSATOR, EnergyCondensatorBlockEntityRenderer::new);

        HandledScreens.register(ModScreenHandlers.FUSION_SMITHING_SCREEN_HANDLER, FusionSmithingScreen::new);

        ParticleFactoryRegistry.getInstance().register(ModParticles.ENERGY_PARTICLE, EnergyParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TRACING_ENERGY_PARTICLE, TracingEnergyParticle.Factory::new);

    }
}
