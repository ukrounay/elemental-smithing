package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModMusicDiscItem extends MusicDiscItem {

    private final Formatting formatting;

    public ModMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds, Formatting formatting) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
        this.formatting = formatting;
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        List<Text> name = Text.of(tooltip.get(0).getString())
                .getWithStyle(Style.EMPTY
                        .withColor(0xFFFFFF)
                        .withFormatting(Formatting.OBFUSCATED)
                        .withFont(new Identifier("minecraft", "illageralt"))
                );
        tooltip.clear();
        tooltip.addAll(name);
        tooltip.add(this.getDescription().formatted(this.formatting));
    }


}
