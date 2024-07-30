package net.ukrounay.elementalsmithing.item.custom;

import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.*;
import net.minecraft.util.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrescribedBookItem extends WrittenBookItem {

    private final ItemStack defaultStack;
    private final BookData bookData;

    public PrescribedBookItem(FabricItemSettings settings, String name) {
        super(settings);
        this.bookData = new BookData(name);
        this.defaultStack = this.bookData.getWrittenBook();
    }

//    @Override
//    public ItemStack getDefaultStack() {
//        return defaultStack;
//    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(bookData.title);
        tooltip.add(bookData.author);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setStackInHand(hand, defaultStack);
        return super.use(world, user, hand);
    }

    public class BookData {

        private final String name;
        public final Text title;
        public final Text author;
        public final int generation;
        public final ArrayList<Text> pages = new ArrayList<>();
        public final int model;

        // Constants for text formatting
        private static final int MAX_LINES_PER_PAGE = 14;
        private static final int MAX_WIDTH = 122; // in pixels
        private static final int AVERAGE_CHAR_WIDTH = 6; // Approximation


        public BookData(String name) {
            this.name = name;
            String jsonFilePath = "/assets/" + ElementalSmithing.MOD_ID + "/books/" + this.name + ".json";
            InputStream inputStream = BookData.class.getResourceAsStream(jsonFilePath);
            if (inputStream == null) {
                ElementalSmithing.LOGGER.warn("Failed to load JSON file: " + jsonFilePath);
                this.title = Text.empty();
                this.author = Text.empty();
                this.generation = 0;
                this.model = 0;
                return;
            }

            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
            this.title = parseTextComponent(JsonHelper.getElement(jsonObject, "title"));
            this.author = parseTextComponent(JsonHelper.getElement(jsonObject, "author"));
            this.generation = JsonHelper.getInt(jsonObject, "generation", 3);
            this.model = JsonHelper.getInt(jsonObject, "custom_model_data", 0);

            JsonArray pagesJsonArray = JsonHelper.getArray(jsonObject, "pages");
            for (JsonElement pageElement : pagesJsonArray) {
                pages.add(parsePage(pageElement));
//                pages.addAll(splitPage(parsePage(pageElement)));
            }
        }

        // Method to parse a page from JSON element
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

        // Method to split text into multiple pages
//        private static List<Text> splitPage(Text pageText) {
//            List<Text> pages = new ArrayList<>();
//
//            String fullText = pageText.getString();
//            String[] words = fullText.split(" ");
//            List<String> currentLines = new ArrayList<>();
//            StringBuilder currentLine = new StringBuilder();
//
//            for (String word : words) {
//                if ((currentLine.length() + word.length()) * AVERAGE_CHAR_WIDTH > MAX_WIDTH) {
//                    currentLines.add(currentLine.toString().trim());
//                    currentLine = new StringBuilder();
//                }
//                currentLine.append(word).append(" ");
//            }
//            currentLines.add(currentLine.toString().trim());
//
//            List<Text> currentPageLines = new ArrayList<>();
//            for (String line : currentLines) {
//                currentPageLines.add(Text.literal(line));
//                if (currentPageLines.size() >= MAX_LINES_PER_PAGE) {
//                    pages.add(Texts.join(currentPageLines, Text.literal("\n")));
//                    currentPageLines.clear();
//                }
//            }
//            if (!currentPageLines.isEmpty()) {
//                pages.add(Texts.join(currentPageLines, Text.literal("\n")));
//            }
//
//            return pages;
//        }


        // Method to parse text component from JSON object
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

        public ItemStack getWrittenBook() {
            ItemStack book = new ItemStack(Items.WRITTEN_BOOK);

            NbtCompound tag = new NbtCompound();

            tag.putString("title", this.title.getString());
            tag.putString("author", this.author.getString());
            tag.putInt("generation", this.generation);

            NbtList pagesList = new NbtList();
            for (Text page : this.pages) {
                pagesList.add(NbtString.of(Text.Serializer.toJson(page)));
            }

            tag.put("pages", pagesList);
            tag.putFloat("CustomModelData", this.model);

            book.setNbt(tag);

            return book;
        }
    }

}
