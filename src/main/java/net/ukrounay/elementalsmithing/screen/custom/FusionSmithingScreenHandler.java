package net.ukrounay.elementalsmithing.screen.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.ukrounay.elementalsmithing.block.custom.FusionSmithingTableBlock;
import net.ukrounay.elementalsmithing.block.entity.EnergyCondensatorBlockEntity;
import net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity;
import net.ukrounay.elementalsmithing.screen.ModScreenHandlers;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import static net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity.*;

public class FusionSmithingScreenHandler extends ScreenHandler {

    protected final FusionSmithingTableBlockEntity entity;
    protected final PlayerEntity player;
    protected final Inventory inventory;
//    public boolean craftingMode;

    private final Vector2i slotBox;

    public FusionSmithingScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public FusionSmithingScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.FUSION_SMITHING_SCREEN_HANDLER, syncId);
        this.entity = (FusionSmithingTableBlockEntity) blockEntity;
        this.player = playerInventory.player;
        this.inventory = ((Inventory) blockEntity);
//        this.craftingMode = false;
        this.slotBox = new Vector2i(22, 18);
        addInputSlots(inventory);
        addResultSlot(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty())  slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

//    @Override
//    public boolean onButtonClick(PlayerEntity player, int id) {
//        if (id == 0 && canSwitchMode()) {
//            craftingMode = !craftingMode;
//            return true;
//        }
//        return super.onButtonClick(player, id);
//    }

    private void addInputSlots(Inventory inventory) {
        addSlot(new Slot(inventory, FUSION_INGREDIENT_LEFT, slotBox.x, slotBox.y + 26));
        addSlot(new Slot(inventory, FUSION_INGREDIENT_RIGHT, slotBox.x + 116, slotBox.y + 26));
        addCraftingSlot(inventory, FUSION_CRAFT_1, 35, 3);
        addCraftingSlot(inventory, FUSION_CRAFT_2, 58, 0);
        addCraftingSlot(inventory, FUSION_CRAFT_3, 81, 3);
        addCraftingSlot(inventory, FUSION_CRAFT_4, 32, 26);
        addCraftingSlot(inventory, FUSION_CRAFT_5, 84, 26);
        addCraftingSlot(inventory, FUSION_CRAFT_6, 35, 49);
        addCraftingSlot(inventory, FUSION_CRAFT_7, 58, 52);
        addCraftingSlot(inventory, FUSION_CRAFT_8, 81, 49);
    }

    private void addCraftingSlot(Inventory inv, int i, int x, int y) {
        this.addSlot(new Slot(inv, i, slotBox.x + x, slotBox.y + y) {

        });
    }

    private void addResultSlot(Inventory inventory) {
        this.addSlot(new Slot(inventory, FUSION_OUTPUT, slotBox.x + 58, slotBox.y + 26) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
            
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                FusionSmithingScreenHandler.this.onTakeOutput(player, stack);
            }
        });
    }


    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraft(player.getWorld(), player, stack.getCount());
        this.decrementStack(FUSION_INGREDIENT_LEFT);
        this.decrementStack(FUSION_INGREDIENT_RIGHT);
        this.decrementStack(FUSION_CRAFT_1);
        this.decrementStack(FUSION_CRAFT_2);
        this.decrementStack(FUSION_CRAFT_3);
        this.decrementStack(FUSION_CRAFT_4);
        this.decrementStack(FUSION_CRAFT_5);
        this.decrementStack(FUSION_CRAFT_6);
        this.decrementStack(FUSION_CRAFT_7);
        this.decrementStack(FUSION_CRAFT_8);
        this.decrementStack(FUSION_OUTPUT);
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        if (!itemStack.isEmpty()) {
            var item = itemStack.getItem().asItem();
            if(item.hasRecipeRemainder()) {
                this.inventory.setStack(slot, item.getRecipeRemainder().getDefaultStack());
            } else {
                itemStack.decrement(1);
                this.inventory.setStack(slot, itemStack);
            }
        }
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }


    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 98 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 156));
        }
    }

    public boolean isFusingIngredientInLeftSlot() {
        Slot slot = this.slots.get(FUSION_INGREDIENT_LEFT);
        return slot != null && slot.hasStack();
    }

    public boolean isFusingIngredientInRightSlot() {
        Slot slot = this.slots.get(FUSION_INGREDIENT_RIGHT);
        return slot != null && slot.hasStack();
    }

    public boolean isFusingResultInOutputSlot() {
        Slot slot = this.slots.get(FUSION_OUTPUT);
        return slot != null && slot.hasStack();
    }

//    public boolean canSwitchMode() {
//        return FusionSmithingTableBlock.getPower(entity.getWorld(), entity.getPos()) > 0;
//    }
}
