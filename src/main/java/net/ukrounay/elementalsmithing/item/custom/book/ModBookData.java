package net.ukrounay.elementalsmithing.item.custom.book;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.ukrounay.elementalsmithing.ElementalSmithing;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ModBookData {

    public final Text title;
    public final Text author;
    public final int generation;
    public final int model;
    public final List<Text> pages = new ArrayList<>();
    public final List<SummaryEntry> summary = new ArrayList<>();

    public ModBookData(String name) {
        String path = "/assets/" + ElementalSmithing.MOD_ID + "/books/" + name + ".json";
        InputStream stream = ModBookData.class.getResourceAsStream(path);

        if (stream == null) {
            ElementalSmithing.LOGGER.warn("Failed to load book JSON: {}", path);
            this.title = Text.literal(name);
            this.author = Text.empty();
            this.generation = 0;
            this.model = 0;
            return;
        }

        JsonObject json = JsonParser.parseReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();

        this.title = parseText(json.get("title"), Text.literal(name));
        this.author = parseText(json.get("author"), Text.empty());
        this.generation = JsonHelper.getInt(json, "generation", 0);
        this.model = JsonHelper.getInt(json, "custom_model_data", 0);

        if (json.has("pages")) {
            for (JsonElement pageElement : json.getAsJsonArray("pages")) {
//                    pages.add(parseText(pageElement, Text.empty()));
                pages.add(parsePage(pageElement));
            }

        }

        // Optional table-of-contents. "page" is 0-based into `pages`;
        // it gets offset by +1 internally since page 0 in the screen is
        // reserved for the contents view itself.
        if (json.has("summary")) {
            for (JsonElement entryElement : json.getAsJsonArray("summary")) {
                JsonObject entryObj = entryElement.getAsJsonObject();
                Text label = parseText(entryObj.get("label"), Text.literal("?"));
                int targetPage = JsonHelper.getInt(entryObj, "page", 0) + 1;
                Identifier iconId = new Identifier(
                        JsonHelper.getString(entryObj, "icon", "minecraft:book"));
                summary.add(new SummaryEntry(label, iconId, targetPage));
            }
        }
    }

    /**
     * Mirrors how vanilla deserializes a page/title/author json element into Text.
     */
    private static Text parseText(JsonElement element, Text fallback) {
        if (element == null || element.isJsonNull()) return fallback;
        Text parsed = Text.Serializer.fromJson(element);
        return parsed != null ? parsed : fallback;
    }

    private static Text parsePage(JsonElement pageElement) {
        if (pageElement.isJsonArray()) {
            JsonArray jsonArray = pageElement.getAsJsonArray();
            MutableText pageText = Text.literal("");
            for (JsonElement element : jsonArray) {
                pageText.append(parseTextComponent(element));
            }
            return pageText;
        } else return parseTextComponent(pageElement);
    }

    private static Text parseTextComponent(JsonElement textElement) {
        if (textElement.isJsonObject()) {
            JsonObject jsonObject = textElement.getAsJsonObject();

            String text = JsonHelper.getString(jsonObject, "text");
            MutableText textComponent = Text.literal(text);

            if (jsonObject.has("color")) {
                textComponent.setStyle(textComponent.getStyle().withColor(TextColor.parse(jsonObject.get("color").getAsString())));
            }
            if (jsonObject.has("bold")) {
                textComponent.setStyle(textComponent.getStyle().withBold(jsonObject.get("bold").getAsBoolean()));
            }
            if (jsonObject.has("italic")) {
                textComponent.setStyle(textComponent.getStyle().withItalic(jsonObject.get("italic").getAsBoolean()));
            }
            if (jsonObject.has("font")) {
                textComponent.setStyle(textComponent.getStyle().withFont(new Identifier(jsonObject.get("font").getAsString())));
            }
            if (jsonObject.has("obfuscated")) {
                textComponent.setStyle(textComponent.getStyle().withObfuscated(jsonObject.get("obfuscated").getAsBoolean()));
            }
            return textComponent;
        } else {
            return Text.literal(textElement.getAsString());
        }
    }


    /**
     * Builds a real written-book ItemStack (e.g. for /give, ground-item display, etc.).
     */
    public ItemStack toWrittenBookStack() {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        NbtCompound tag = new NbtCompound();
        tag.putString("title", title.getString());
        tag.putString("author", author.getString());
        tag.putInt("generation", generation);

        NbtList pagesList = new NbtList();
        for (Text page : pages) {
            pagesList.add(NbtString.of(Text.Serializer.toJson(page)));
        }
        tag.put("pages", pagesList);
        if (model != 0) tag.putInt("CustomModelData", model);
        book.setNbt(tag);
        return book;
    }

    public record SummaryEntry(Text label, Identifier icon, int targetPage) {
        public ItemStack iconStack() {
            Item item = Registries.ITEM.get(icon);
            return new ItemStack(item);
        }
    }

}





