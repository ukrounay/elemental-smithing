package net.ukrounay.elementalsmithing.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class ModParticles {

    public static final DefaultParticleType ENERGY_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType SPIRAL_ENERGY_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType TRACING_ENERGY_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType ENERGY_FLUCTUATION_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ElementalSmithing.MOD_ID, "energy"), ENERGY_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ElementalSmithing.MOD_ID, "spiral_energy"), SPIRAL_ENERGY_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ElementalSmithing.MOD_ID, "tracing_energy"), TRACING_ENERGY_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ElementalSmithing.MOD_ID, "energy_fluctuation"), ENERGY_FLUCTUATION_PARTICLE);
    }

}
