package net.ukrounay.elementalsmithing.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

public class FusionSmithingScreen extends HandledScreen<FusionSmithingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ElementalSmithing.MOD_ID, "textures/gui/fusion_smithing_table.png");

    public FusionSmithingScreen(FusionSmithingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 4;
        titleX = 43;
        playerInventoryTitleY = 1000;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        backgroundWidth = 176;
        backgroundHeight = 176;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderLeaves(context, x, y);
    }

    private void renderLeaves(DrawContext context, int x, int y) {

        if(handler.isFusingIngredientInLeftSlot()) {
            context.drawTexture(TEXTURE, x + 9, y + 14, 0, 176, 41, 76 );
        }
        if(handler.isFusingIngredientInRightSlot()) {
            context.drawTexture(TEXTURE, x + 126, y + 14, 41, 176, 41, 76 );
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
