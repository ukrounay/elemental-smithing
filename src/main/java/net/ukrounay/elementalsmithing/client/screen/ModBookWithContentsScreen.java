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
public class ModBookWithContentsScreen extends Screen {

    private static final Identifier BOOK_TEXTURE =
            new Identifier("textures/gui/book.png"); // vanilla asset, 192x192

    private static final int TEXTURE_WIDTH = 192;
    private static final int TEXTURE_HEIGHT = 192;
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_X = 36;
    private static final int TEXT_Y = 18;
    private static final int ENTRY_HEIGHT = 18;
    private static final int ENTRY_ICON_SIZE = 16;

    // Contents-page list viewport, relative to the book's top-left (x, y).
    private static final int LIST_TOP = TEXT_Y + 14;
    private static final int LIST_BOTTOM = 154;
    private static final int SCROLLBAR_WIDTH = 2;

    private static final int COLOR_TEXT = 0x000000;
    private static final int COLOR_TEXT_HOVER = 0x3333AA;
    private static final int COLOR_HOVER_BG = 0x30000000;
    private static final int COLOR_SCROLLBAR_TRACK = 0x80000000;
    private static final int COLOR_SCROLLBAR_HANDLE = 0xFFC0C0C0;

    private final ModBookData data;
    private final boolean hasContents;
    private final List<List<OrderedText>> pages = new ArrayList<>();
    private int pageIndex = 0;

    /** Vertical scroll offset (in pixels) for the contents list. */
    private int scrollOffset = 0;

    private PageTurnWidget prevPageButton;
    private PageTurnWidget nextPageButton;

    public ModBookWithContentsScreen(ModBookData data) {
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

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.close()).dimensions(this.width / 2 - 100, 196, 200, 20).build());

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
                x + TEXTURE_WIDTH / 2 - textRenderer.getWidth(pageNumber) / 2, y + 159, 0x808080, false);

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
        int listRight = x + TEXT_X + TEXT_WIDTH;

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

        renderScrollbar(context, listLeft, listTop, listRight, listBottom);
    }

    /** Vanilla list scrollbars are two filled rects (track + handle), not a texture. */
    private void renderScrollbar(DrawContext context, int left, int top, int right, int bottom) {
        int max = maxScroll();
        if (max <= 0) return; // content fits, no bar needed — matches vanilla behavior

        int trackX = right - SCROLLBAR_WIDTH - 1;
        context.fill(trackX, top, trackX + SCROLLBAR_WIDTH, bottom, COLOR_SCROLLBAR_TRACK);

        int viewHeight = bottom - top;
        int handleHeight = Math.max(8, viewHeight * viewHeight / contentsHeight());
        int handleY = top + (int) ((viewHeight - handleHeight) * (scrollOffset / (float) max));

        context.fill(trackX, handleY, trackX + SCROLLBAR_WIDTH, handleY + handleHeight, COLOR_SCROLLBAR_HANDLE);
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