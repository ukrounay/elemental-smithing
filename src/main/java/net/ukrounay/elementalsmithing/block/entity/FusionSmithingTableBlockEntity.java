package net.ukrounay.elementalsmithing.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.recipe.FusionSmithingRecipe;
import net.ukrounay.elementalsmithing.screen.FusionSmithingScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FusionSmithingTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);

    public static final int FUSION_INGREDIENT_LEFT = 0;
    public static final int FUSION_INGREDIENT_RIGHT = 1;
    public static final int FUSION_CRAFT_1 = 2;
    public static final int FUSION_CRAFT_2 = 3;
    public static final int FUSION_CRAFT_3 = 4;
    public static final int FUSION_CRAFT_4 = 5;
    public static final int FUSION_CRAFT_5 = 6;
    public static final int FUSION_CRAFT_6 = 7;
    public static final int FUSION_CRAFT_7 = 8;
    public static final int FUSION_CRAFT_8 = 9;
    public static final int FUSION_OUTPUT = 10;
    private boolean isUpdating = false;


    public FusionSmithingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FUSION_SMITHING_TABLE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        update(world);
    }
    @Override
    public Text getDisplayName() {
        return Text.translatable("gui." + ElementalSmithing.MOD_ID + ".fusion_smithing_table.title");
    }
    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FusionSmithingScreenHandler(syncId, playerInventory, this);
    }

    public void update(World world) {
        if(world.isClient() || this.isUpdating) return;
        this.isUpdating = true;
        if(this.hasRecipe()) this.showResult();
        else this.setStack(FUSION_OUTPUT, ItemStack.EMPTY);
        this.isUpdating = false;
    }

    private void showResult() {
        Optional<FusionSmithingRecipe> recipe = getCurrentRecipe();
        ElementalSmithing.LOGGER.info("crafting");
        this.setStack(FUSION_OUTPUT, new ItemStack(recipe.get().getOutput(null).getItem(),
                recipe.get().getOutput(null).getCount()));
    }

    private boolean hasRecipe() {
        Optional<FusionSmithingRecipe> recipe = getCurrentRecipe();
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(recipe.get().getOutput(null))
                && canInsertItemIntoOutputSlot(recipe.get().getOutput(null).getItem());
    }
    private Optional<FusionSmithingRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for(int i = 0; i < this.size(); i++) inv.setStack(i, this.getStack(i));
        return getWorld().getRecipeManager().getFirstMatch(FusionSmithingRecipe.Type.INSTANCE, inv, getWorld());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(FUSION_OUTPUT).getItem() == item || this.getStack(FUSION_OUTPUT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(FUSION_OUTPUT).getCount() + result.getCount() <= getStack(FUSION_OUTPUT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(FUSION_OUTPUT).isEmpty() || this.getStack(FUSION_OUTPUT).getCount() < this.getStack(FUSION_OUTPUT).getMaxCount();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public int size() {
        return getItems().size();
    }

}
