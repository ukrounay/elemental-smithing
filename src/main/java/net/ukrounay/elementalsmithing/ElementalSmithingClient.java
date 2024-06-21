package net.ukrounay.elementalsmithing;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.particle.FireSmokeParticle;
import net.ukrounay.elementalsmithing.particles.ModParticles;
import net.ukrounay.elementalsmithing.screen.FusionSmithingScreen;
import net.ukrounay.elementalsmithing.screen.ModScreenHandlers;

public class ElementalSmithingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FUSION_SMITHING_SCREEN_HANDLER, FusionSmithingScreen::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.FLAME_PARTICLE, FireSmokeParticle.Factory::new);
    }
}
