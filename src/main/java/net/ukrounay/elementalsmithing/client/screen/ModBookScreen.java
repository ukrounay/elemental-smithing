package net.ukrounay.elementalsmithing.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.ukrounay.elementalsmithing.item.custom.book.ModBookData;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive book screen. Page 0 is a clickable, scrollable table of
 * contents (only present if the book's json defines "summary" entries);
 * real pages follow starting at index 1. If there's no summary, page 0 is
 * just the first real page, same as before.
 */
public class ModBookScreen extends Screen {

    private static final Identifier BOOK_TEXTURE =
            new Identifier("textures/gui/book.png"); // vanilla asset, 192x192

    // Same scroller sprite vanilla's own creative-inventory item list uses.
    // U=232 is the "enabled/draggable" state, U=244 is the greyed-out
    // "nothing to scroll" state — check against decompiled source for your
    // exact version if it looks off; sprite atlas coords can shift between
    // Minecraft releases.
    private static final Identifier SCROLLER_TEXTURE =
            new Identifier("textures/gui/container/creative_inventory/tabs.png");
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int SCROLLER_U_ACTIVE = 232;
    private static final int SCROLLER_U_INACTIVE = 244;

    private static final int TEXTURE_WIDTH = 192;
    private static final int TEXTURE_HEIGHT = 192;
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_X = 36;
    private static final int TEXT_Y = 18;
    private static final int ENTRY_HEIGHT = 18;
    private static final int ENTRY_ICON_SIZE = 16;

    // Contents-page list viewport, relative to the book's top-left (x, y).
    private static final int LIST_TOP = TEXT_Y + 12;
    private static final int LIST_BOTTOM = 154;
    private static final int SCROLLBAR_GAP = 2; // space between text column and scrollbar

    private static final int COLOR_TEXT = 0x000000;
    private static final int COLOR_TEXT_HOVER = 0x3333AA;
    private static final int COLOR_HOVER_BG = 0x30000000;

    private final ModBookData data;
    private final boolean hasContents;
    private final List<List<OrderedText>> pages = new ArrayList<>();
    private int pageIndex = 0;

    /** Vertical scroll offset (in pixels) for the contents list. */
    private int scrollOffset = 0;
    private boolean draggingScrollbar = false;

    private ButtonWidget prevPageButton;
    private ButtonWidget nextPageButton;

    public ModBookScreen(ModBookData data) {
        super(data.title);
        this.data = data;
        this.hasContents = !data.summary.isEmpty();
    }

    @Override
    protected void init() {
        super.init();

        // textRenderer only exists from here on, not in the constructor.
        pages.clear();
        for (Text page : data.pages) {
            pages.add(textRenderer.wrapLines(page, TEXT_WIDTH));
        }
        if (pages.isEmpty()) pages.add(List.of());

        pageIndex = MathHelper.clamp(pageIndex, 0, maxPageIndex());
        scrollOffset = 0;

        int x = (width - TEXTURE_WIDTH) / 2;
        int y = (height - TEXTURE_HEIGHT) / 2;

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).dimensions(x + this.width / 2 - 100, y + 196, 200, 20).build());

        this.nextPageButton = (PageTurnWidget)this.addDrawableChild(new PageTurnWidget(x + 116, y + 159, true, (button) -> this.flipPage(1), true));
        this.prevPageButton = (PageTurnWidget)this.addDrawableChild(new PageTurnWidget(x + 43, y + 159, false, (button) -> this.flipPage(-1), true));


        updateButtons();
    }

    private int maxPageIndex() {
        return (hasContents ? 1 : 0) + pages.size() - 1;
    }

    private boolean isOnContentsPage() {
        return hasContents && pageIndex == 0;
    }

    /** Index into `pages`, correcting for the contents page occupying slot 0. */
    private int realPageIndex() {
        return hasContents ? pageIndex - 1 : pageIndex;
    }

    private void flipPage(int direction) {
        int newPage = pageIndex + direction;
        if (newPage >= 0 && newPage <= maxPageIndex()) {
            pageIndex = newPage;
            updateButtons();
        }
    }

    private void jumpToPage(int targetPage) {
        pageIndex = MathHelper.clamp(targetPage, 0, maxPageIndex());
        updateButtons();
    }

    private void updateButtons() {
        prevPageButton.visible = pageIndex > 0;
        nextPageButton.visible = pageIndex < maxPageIndex();
    }

    private int contentsHeight() {
        return data.summary.size() * ENTRY_HEIGHT;
    }

    private int viewportHeight() {
        return LIST_BOTTOM - LIST_TOP;
    }

    private int maxScroll() {
        return Math.max(0, contentsHeight() - viewportHeight());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);

        int x = (width - TEXTURE_WIDTH) / 2;
        int y = (height - TEXTURE_HEIGHT) / 2;
        context.drawTexture(BOOK_TEXTURE, x, y, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        if (isOnContentsPage()) {
            renderContents(context, x, y, mouseX, mouseY);
        } else {
            renderPage(context, x, y);
        }

        Text pageNumber = Text.of((pageIndex + 1) + " / " + (maxPageIndex() + 1));
        context.drawText(textRenderer, pageNumber,
                x + (TEXTURE_WIDTH - textRenderer.getWidth(pageNumber)) / 2, y + 159,
                0x808080, false);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderPage(DrawContext context, int x, int y) {
        List<OrderedText> lines = pages.get(realPageIndex());
        for (int i = 0; i < lines.size(); i++) {
            context.drawText(textRenderer, lines.get(i),
                    x + TEXT_X, y + TEXT_Y + i * 9, COLOR_TEXT, false);
        }
    }

    private void renderContents(DrawContext context, int x, int y, int mouseX, int mouseY) {
        context.drawText(textRenderer, Text.translatable("book.elementalsmithing.contents"),
                x + TEXT_X, y + TEXT_Y, COLOR_TEXT, false);

        int listTop = y + LIST_TOP;
        int listBottom = y + LIST_BOTTOM;
        int listLeft = x + TEXT_X - 2;
        int listRight = x + TEXT_X + TEXT_WIDTH - SCROLLBAR_GAP - SCROLLER_WIDTH;

        // Same technique EntryListWidget uses: clip to the viewport so rows
        // scrolled past the top/bottom edge don't render outside the list.
        context.enableScissor(listLeft, listTop, listRight, listBottom);

        for (int i = 0; i < data.summary.size(); i++) {
            int entryY = listTop + i * ENTRY_HEIGHT - scrollOffset;
            if (entryY + ENTRY_HEIGHT < listTop || entryY > listBottom) {
                continue; // fully outside the viewport, skip drawing it
            }

            ModBookData.SummaryEntry entry = data.summary.get(i);
            boolean hovered = mouseX >= listLeft && mouseX <= listRight
                    && mouseY >= Math.max(entryY, listTop)
                    && mouseY <= Math.min(entryY + ENTRY_HEIGHT, listBottom);

            if (hovered) {
                context.fill(listLeft, entryY, listRight, entryY + ENTRY_HEIGHT, COLOR_HOVER_BG);
            }

            context.drawItem(entry.iconStack(), x + TEXT_X, entryY + 1);
            context.drawText(textRenderer, entry.label(),
                    x + TEXT_X + ENTRY_ICON_SIZE + 4,
                    entryY + (ENTRY_HEIGHT - textRenderer.fontHeight) / 2,
                    hovered ? COLOR_TEXT_HOVER : COLOR_TEXT, false);
        }

        context.disableScissor();

        renderScrollbar(context, listTop);
    }

    /** Left edge of the scrollbar sprite, for a book positioned at book-x `x`. */
    private int scrollbarX(int x) {
        return x + TEXT_X + TEXT_WIDTH - SCROLLER_WIDTH - SCROLLBAR_GAP;
    }

    /** Y position (top-left) of the draggable handle, clamped within the track. */
    private int handleY(int listTop) {
        int max = maxScroll();
        int track = viewportHeight() - SCROLLER_HEIGHT;
        if (max <= 0 || track <= 0) return listTop;
        return listTop + (int) (track * (scrollOffset / (float) max));
    }

    private void renderScrollbar(DrawContext context, int listTop) {
        int x = (width - TEXTURE_WIDTH) / 2;
        int barX = scrollbarX(x);
        int barY = handleY(listTop);
        int u = maxScroll() > 0 ? SCROLLER_U_ACTIVE : SCROLLER_U_INACTIVE;

        context.drawTexture(SCROLLER_TEXTURE, barX, barY, u, 0, SCROLLER_WIDTH, SCROLLER_HEIGHT);
    }

    /** True if (mouseX, mouseY) is over the scrollbar handle's current position. */
    private boolean isOverScrollbarHandle(double mouseX, double mouseY, int x, int listTop) {
        int barX = scrollbarX(x);
        int barY = handleY(listTop);
        return mouseX >= barX && mouseX <= barX + SCROLLER_WIDTH
                && mouseY >= barY && mouseY <= barY + SCROLLER_HEIGHT;
    }

    /** True if (mouseX, mouseY) is anywhere in the scrollbar's vertical track. */
    private boolean isOverScrollbarTrack(double mouseX, double mouseY, int x, int listTop, int listBottom) {
        int barX = scrollbarX(x);
        return mouseX >= barX && mouseX <= barX + SCROLLER_WIDTH
                && mouseY >= listTop && mouseY <= listBottom;
    }

    /** Sets scrollOffset so the handle centers on mouseY, then clamps to range. */
    private void updateScrollFromMouse(double mouseY, int listTop) {
        int max = maxScroll();
        if (max <= 0) return;
        int track = viewportHeight() - SCROLLER_HEIGHT;
        if (track <= 0) return;

        double relativeY = mouseY - listTop - SCROLLER_HEIGHT / 2.0;
        double fraction = MathHelper.clamp(relativeY / track, 0.0, 1.0);
        scrollOffset = (int) Math.round(fraction * max);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isOnContentsPage()) {
            int x = (width - TEXTURE_WIDTH) / 2;
            int y = (height - TEXTURE_HEIGHT) / 2;
            int listTop = y + LIST_TOP;
            int listBottom = y + LIST_BOTTOM;
            int listLeft = x + TEXT_X - 2;
            int listRight = x + TEXT_X + TEXT_WIDTH;

            if (maxScroll() > 0 && isOverScrollbarTrack(mouseX, mouseY, x, listTop, listBottom)) {
                draggingScrollbar = true;
                if (!isOverScrollbarHandle(mouseX, mouseY, x, listTop)) {
                    // Clicked the track, not the handle itself — jump straight there.
                    updateScrollFromMouse(mouseY, listTop);
                }
                return true;
            }

            for (int i = 0; i < data.summary.size(); i++) {
                int entryY = listTop + i * ENTRY_HEIGHT - scrollOffset;
                if (entryY + ENTRY_HEIGHT < listTop || entryY > listBottom) continue;

                if (mouseX >= listLeft && mouseX <= listRight
                        && mouseY >= Math.max(entryY, listTop)
                        && mouseY <= Math.min(entryY + ENTRY_HEIGHT, listBottom)) {
                    jumpToPage(data.summary.get(i).targetPage());
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingScrollbar) {
            int y = (height - TEXTURE_HEIGHT) / 2;
            updateScrollFromMouse(mouseY, y + LIST_TOP);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // 1.20.1 signature: (mouseX, mouseY, amount). If your mappings expose
        // a 4-arg (horizontal, vertical) overload instead, use the vertical one here.
        if (isOnContentsPage()) {
            int max = maxScroll();
            if (max > 0) {
                scrollOffset = MathHelper.clamp(scrollOffset - (int) (amount * ENTRY_HEIGHT), 0, max);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 263) { flipPage(-1); return true; } // GLFW_KEY_LEFT
        if (keyCode == 262) { flipPage(1); return true; }  // GLFW_KEY_RIGHT
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}