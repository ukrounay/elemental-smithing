package net.ukrounay.elementalsmithing.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.screen.custom.FusionSmithingScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FusionSmithingScreenHandler> FUSION_SMITHING_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(ElementalSmithing.MOD_ID, "fusion_smithing"),
                    new ExtendedScreenHandlerType<>(FusionSmithingScreenHandler::new));

    public static void registerScreenHandlers() {
        ElementalSmithing.LOGGER.info("Registering Screen Handlers for " + ElementalSmithing.MOD_ID);
    }
}
