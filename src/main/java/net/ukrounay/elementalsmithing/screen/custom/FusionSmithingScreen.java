package net.ukrounay.elementalsmithing.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.item.custom.ElementalCoreItem;

public class FusionSmithingScreen extends HandledScreen<FusionSmithingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ElementalSmithing.MOD_ID, "textures/gui/fusion_smithing_table.png");

    public FusionSmithingScreen(FusionSmithingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        backgroundWidth = 176;
        backgroundHeight = 180;
        titleX = backgroundWidth / 2;
        titleY = -4;

    }

//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (isPointWithinBounds(28, 79, 21, 15, mouseX, mouseY) && handler.onButtonClick(client.player, 0)) {
//            client.interactionManager.clickButton(handler.syncId, 0);
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
//        drawSwitch(context, x, y, mouseX, mouseY);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

//    private void drawButtonTooltip(DrawContext context, int mouseX, int mouseY) {
//        if(this.isPointWithinBounds(150, 1, 15, 14, mouseX, mouseY)) {
//            context.drawTooltip(textRenderer, getButtonTooltip(), mouseX, mouseY);
//        }
//    }

//    private Text getSwitchText() {
//        return Text.translatable("gui." + ElementalSmithing.MOD_ID + ".fusion_smithing_table.tooltip.mode_switch_" + (handler.craftingMode ? "on" : "off"));
//    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        context.drawTexture(TEXTURE, x + backgroundWidth / 2 - 46, y - 8, 164, 240, 92, 16);
        drawSlots(context, x, y);
        drawLeaves(context, x, y);
        RenderSystem.disableBlend();
    }

//    private void drawSwitch(DrawContext context, int x, int y, int mouseX, int mouseY) {
//        if (handler.canSwitchMode()) {
//            Boolean hover = isPointWithinBounds(28, 79, 21, 15, mouseX, mouseY);
//
//            int bgTextureOffset = handler.craftingMode ? 24 : 14;
//            int fgTextureOffset = hover ? 14 : 0;
//            int fgOffset = handler.craftingMode ? 7 : 0;
//            int iconTextureOffset = handler.craftingMode ? 16 : 8;
//
//            context.drawTexture(TEXTURE, x + 28, y + 82, 220, bgTextureOffset,21, 10);
//            context.drawTexture(TEXTURE, x + 28 + fgOffset, y + 80, 220 + fgTextureOffset, 0,14, 14);
//            context.drawTexture(TEXTURE, x + 31 + fgOffset, y + 83, 248, iconTextureOffset,8, 8);
//        }
//        context.drawText(textRenderer, getSwitchText(), x + 52, y + 84, handler.craftingMode ? 0xFFD50B : 0xF1F1F1,false);
//    }

    private enum SlotType {
        INPUT_SLOT(176, 0, 18, 18, 1),
        ACTIVE_INPUT_SLOT(176, 18, 18, 18, 1),
        OUTPUT_SLOT(194, 0, 26, 26, 5),
        ACTIVE_OUTPUT_SLOT(194, 26, 26, 26, 5);

        public final int u;
        public final int v;
        public final int height;
        public final int width;
        public final int offset;

        SlotType(int u, int v, int height, int width, int offset) {

            this.u = u;
            this.v = v;
            this.height = height;
            this.width = width;
            this.offset = offset;
        }
    }

    private void drawSlots(DrawContext context, int x, int y) {
        drawSlot(context, x, y, 0, handler.isFusingIngredientInLeftSlot() ? SlotType.ACTIVE_INPUT_SLOT : SlotType.INPUT_SLOT);
        drawSlot(context, x, y, 1, handler.isFusingIngredientInRightSlot() ? SlotType.ACTIVE_INPUT_SLOT : SlotType.INPUT_SLOT);
        for (int i = 2; i < 10; i++)
            drawSlot(context, x, y, i, SlotType.INPUT_SLOT);
//        if (!handler.craftingMode)
//            RenderSystem.setShaderColor(1,1,1,0.3f);
//
//        RenderSystem.setShaderColor(1,1,1,1);

        drawSlot(context, x, y, 10, handler.isFusingResultInOutputSlot() ? SlotType.ACTIVE_OUTPUT_SLOT : SlotType.OUTPUT_SLOT);
    }
    private void drawSlot(DrawContext context, int x, int y, int i, SlotType slotType) {
        Slot slot = handler.getSlot(i);
        if (slot != null) context.drawTexture(TEXTURE,
                x + slot.x - slotType.offset, y + slot.y - slotType.offset,
                slotType.u, slotType.v, slotType.width, slotType.height
        );
    }

    private void drawLeaves(DrawContext context, int x, int y) {
        context.drawTexture(TEXTURE, x + 25, y - 5, 164, 180, 28, 18);
        context.drawTexture(TEXTURE, x + 126, y - 7, 164, 198, 23, 17);
        y += 5;
        context.drawTexture(TEXTURE, x + 9, y + 9,
                handler.isFusingIngredientInLeftSlot() ? 0 : 82, 180, 41, 76);
        context.drawTexture(TEXTURE, x + 126, y + 9,
                handler.isFusingIngredientInRightSlot() ? 41 : 123, 180, 41, 76);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.titleX, this.titleY, 0xFFD50B);
    }
}
