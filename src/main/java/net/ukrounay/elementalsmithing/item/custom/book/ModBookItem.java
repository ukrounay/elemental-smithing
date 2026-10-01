package net.ukrounay.elementalsmithing.item.custom.book;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import net.minecraft.text.Text;

import java.util.List;


public class ModBookItem extends Item {


    private final ModBookData bookData;

    public ModBookItem(FabricItemSettings settings, String name) {
        super(settings);
        this.bookData = new ModBookData(name);
    }

    public ModBookData getBookData() {
        return bookData;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        tooltip.add(bookData.title);
        tooltip.add(bookData.author);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        // Open our own interactive screen instead of vanilla BookScreen.
        // (No more swapping the held stack for a real written-book stack —
        // the screen reads straight from bookData, so nothing needs to
        // round-trip through NBT just to be displayed.)
        if (world.isClient()) {
            net.ukrounay.elementalsmithing.client.screen.ModBookScreenOpener.open(bookData);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

}