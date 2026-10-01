package net.ukrounay.elementalsmithing.client.screen;

import net.minecraft.client.MinecraftClient;
import net.ukrounay.elementalsmithing.item.custom.book.ModBookData;

public final class ModBookScreenOpener {
    private ModBookScreenOpener() {}

    public static void open(ModBookData data) {
        MinecraftClient.getInstance().setScreen(new ModBookScreen(data));
    }
}