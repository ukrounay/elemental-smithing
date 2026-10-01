package net.ukrounay.elementalsmithing.client.screen;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Loads book content from assets/<namespace>/books/<path>.json as a client
 * resource (so it's supplied by resource packs, same as lang files/textures).
 *
 * Expected JSON shape:
 * {
 *   "title": "My Book",
 *   "pages": [
 *     "First page text.\nSupports newlines.",
 *     "Second page text."
 *   ]
 * }
 */
public final class ModBookContent {
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, BookData> CACHE = new HashMap<>();

    private ModBookContent() {}

    public static BookData get(Identifier bookId) {
        return CACHE.computeIfAbsent(bookId, ModBookContent::load);
    }

    /** Call this (e.g. on ResourceReloadListener) if you want /reload to pick up edits. */
    public static void invalidate(Identifier bookId) {
        CACHE.remove(bookId);
    }

    private static BookData load(Identifier bookId) {
        Identifier resourceId = new Identifier(bookId.getNamespace(),
                "books/" + bookId.getPath() + ".json");

        try {
            Optional<Resource> resource =
                    MinecraftClient.getInstance().getResourceManager().getResource(resourceId);

            if (resource.isEmpty()) {
                return missing(bookId, "file not found: " + resourceId);
            }

            try (Reader reader = new InputStreamReader(
                    resource.get().getInputStream(), StandardCharsets.UTF_8)) {
                BookData data = GSON.fromJson(reader, BookData.class);
                if (data == null || data.pages() == null || data.pages().isEmpty()) {
                    return missing(bookId, "no pages in " + resourceId);
                }
                return data;
            }
        } catch (Exception e) {
            return missing(bookId, e.getMessage());
        }
    }

    private static BookData missing(Identifier bookId, String reason) {
        return new BookData("Missing Book",
                List.of("Could not load book '" + bookId + "': " + reason));
    }

    public record BookData(String title, List<String> pages) {}
}