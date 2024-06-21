package net.ukrounay.elementalsmithing.compat;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.ModBlocks;

import java.util.LinkedList;
import java.util.List;

import static net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity.*;

public class FusionSmithingCategory implements DisplayCategory<BasicDisplay> {

    public static final Identifier TEXTURE =
            new Identifier(ElementalSmithing.MOD_ID,"textures/gui/fusion_smithing_table_recipe.png");
    public static final CategoryIdentifier<FusionSmithingDisplay> FUSION_SMITHING =
            CategoryIdentifier.of(ElementalSmithing.MOD_ID, "fusion_smithing");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return FUSION_SMITHING;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("gui.elementalsmithing.fusion_smithing_table.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.FUSION_SMITHING_TABLE.asItem().getDefaultStack());
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getX(), bounds.getY());
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createTexturedWidget(TEXTURE, new Rectangle(startPoint.x, startPoint.y, 176, 92)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y + 44 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_INGREDIENT_LEFT)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 138, startPoint.y + 44 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_INGREDIENT_RIGHT)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 57, startPoint.y + 21 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_1)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 18 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_2)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 103, startPoint.y + 21 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_3)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 54, startPoint.y + 44 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_4)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 106, startPoint.y + 44 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 57, startPoint.y + 67 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_6)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 70 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_7)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 103, startPoint.y + 67 - 6)).disableBackground()
                .markInput().entries(display.getInputEntries().get(FUSION_CRAFT_8)));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 80, startPoint.y + 44 - 6)).disableBackground()
                .markOutput().entries(display.getOutputEntries().get(0)));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 92;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 176;
    }
}
