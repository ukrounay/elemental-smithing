package net.ukrounay.elementalsmithing.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class ModSounds {

    public static final SoundEvent TEGUKJFLK = registerSoundEvent("tegukjflk");
    public static final SoundEvent IWSEUVHJK = registerSoundEvent("iwseuvhjk");
    public static final SoundEvent ZSDXGHJMR = registerSoundEvent("zsdxghjmr");
    public static final SoundEvent EZSXDFCGH = registerSoundEvent("ezsxdfcgh");
    public static final SoundEvent UBDCWJNK = registerSoundEvent("ubdcwjnk");

    public static final SoundEvent VITAL_ENERGY_ABSORBING = registerSoundEvent("vital_energy_absorbing");
    public static final SoundEvent VITAL_ENERGY_EXTRACTING = registerSoundEvent("vital_energy_extracting");

    public static final SoundEvent FLAME_CORE_COMPLETION = registerSoundEvent("flame_core_completion");
    public static final SoundEvent FROST_CORE_COMPLETION = registerSoundEvent("frost_core_completion");
    public static final SoundEvent SOUL_CORE_COMPLETION = registerSoundEvent("soul_core_completion");

    public static final SoundEvent TERRAFORMING_START = registerSoundEvent("terraforming_start");
    public static final SoundEvent TERRITORY_COMPLETION = registerSoundEvent("territory_completion");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(ElementalSmithing.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));

    }
    public static void registerSounds() {
        ElementalSmithing.LOGGER.info("Registering Sounds for " + ElementalSmithing.MOD_ID);
    }
}
