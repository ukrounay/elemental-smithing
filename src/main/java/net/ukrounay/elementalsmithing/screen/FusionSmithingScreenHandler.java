package net.ukrounay.elementalsmithing.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

import static net.ukrounay.elementalsmithing.block.entity.FusionSmithingTableBlockEntity.*;

public class FusionSmithingScreenHandler extends ScreenHandler {

    protected final PlayerEntity player;
    protected final Inventory inventory;

    public FusionSmithingScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    public FusionSmithingScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.FUSION_SMITHING_SCREEN_HANDLER, syncId);

        this.player = playerInventory.player;

        checkSize(((Inventory) blockEntity), 11);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        addInputSlots(inventory);
        addResultSlot();
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


    private void addInputSlots(Inventory inventory) {
        this.addSlot(new Slot(inventory, FUSION_INGREDIENT_LEFT, 22, 44));
        this.addSlot(new Slot(inventory, FUSION_INGREDIENT_RIGHT, 138, 44));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_1, 57, 21));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_2, 80, 18));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_3, 103, 21));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_4, 54, 44));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_5, 106, 44));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_6, 57, 67));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_7, 80, 70));
        this.addSlot(new Slot(inventory, FUSION_CRAFT_8, 103, 67));
    }

    private void addResultSlot() {
        this.addSlot(new Slot(inventory, FUSION_OUTPUT, 80, 44){

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
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 94 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 152));
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
}
