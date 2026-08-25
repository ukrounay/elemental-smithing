package net.ukrounay.elementalsmithing.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.ukrounay.elementalsmithing.ElementalSmithing;

import static net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity.*;

public class FusionSmithingScreen extends HandledScreen<FusionSmithingScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ElementalSmithing.MOD_ID, "textures/gui/fusion_smithing_table.png");

    private static final Identifier DEMO_INV_TEXTURE = new Identifier("minecraft", "textures/gui/demo_background.png");
    private static final Identifier GENERIC_INV_TEXTURE = new Identifier("minecraft", "textures/gui/container/generic_54.png");

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


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        context.drawNineSlicedTexture(
                DEMO_INV_TEXTURE,
                x,
                y,
                backgroundWidth,
                backgroundHeight,
                5,
                5,
                5,
                5,
                248,
                166,
                0,
                0
        );
        // 248 * 166
        context.drawTexture(TEXTURE, x + backgroundWidth / 2 - 46, y - 8, 164, 240, 92, 16);
        drawSlots(context, x, y);
        drawLeaves(context, x, y);
        RenderSystem.disableBlend();
    }

    private enum SlotType {
        INPUT_SLOT(176, 0, 18, 18, 1),
        ACTIVE_INPUT_SLOT(176, 18, 18, 18, 1),
        OUTPUT_SLOT(194, 0, 26, 26, 5),
        ACTIVE_OUTPUT_SLOT(194, 26, 26, 26, 5),
        PLAYER_INVENTORY_SLOT(7, 139, 18, 18, 1);

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
        for (int i = 0; i < handler.slots.size(); i++) {
            if (i < handler.inventory.size()) {
                drawSlot(context, x, y, i, getSlotType(i), TEXTURE);
            } else {
                drawSlot(context, x, y, i, SlotType.PLAYER_INVENTORY_SLOT, GENERIC_INV_TEXTURE);
            }
        }
    }

    private SlotType getSlotType(int i) {
        return handler.isSlotActive(i)
                ? (i == FUSION_OUTPUT ? SlotType.ACTIVE_OUTPUT_SLOT : SlotType.ACTIVE_INPUT_SLOT)
                : (i == FUSION_OUTPUT ? SlotType.OUTPUT_SLOT : SlotType.INPUT_SLOT);
    }

    private void drawSlot(DrawContext context, int x, int y, int i, SlotType slotType, Identifier texture) {
        Slot slot = handler.getSlot(i);
        if (slot != null) context.drawTexture(texture,
                x + slot.x - slotType.offset, y + slot.y - slotType.offset,
                slotType.u, slotType.v, slotType.width, slotType.height
        );
    }

    private void drawLeaves(DrawContext context, int x, int y) {
        context.drawTexture(TEXTURE, x + 25, y - 5, 164, 180, 28, 18);
        context.drawTexture(TEXTURE, x + 126, y - 7, 164, 198, 23, 17);
        y += 5;
        context.drawTexture(TEXTURE, x + 9, y + 9,
                handler.isSlotActive(FUSION_INGREDIENT_LEFT) ? 0 : 82, 180, 41, 76);
        context.drawTexture(TEXTURE, x + 126, y + 9,
                handler.isSlotActive(FUSION_INGREDIENT_RIGHT) ? 41 : 123, 180, 41, 76);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.titleX, this.titleY, 0xFFD50B);
    }


}
