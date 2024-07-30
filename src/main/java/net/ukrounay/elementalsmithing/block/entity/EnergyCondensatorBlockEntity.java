package net.ukrounay.elementalsmithing.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.custom.EnergyCondensatorBlock;
import net.ukrounay.elementalsmithing.item.custom.ElementalCoreItem;
import net.ukrounay.elementalsmithing.item.custom.ElementalSwordItem;
import net.ukrounay.elementalsmithing.sound.ModSounds;
import net.ukrounay.elementalsmithing.util.ModTags;

public class EnergyCondensatorBlockEntity extends BlockEntity  {

    public static final int maxTicksToCharge = 72;
    public int ticksToCharge = 0;

    public static int calculateTicksToCharge(World world, BlockPos pos) {
        return (int)(maxTicksToCharge * EnergyCondensatorBlock.getPower(world, pos));
    }

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);

    public EnergyCondensatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_CONDENSATOR, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState blockState, EnergyCondensatorBlockEntity entity) {
        var stack = entity.getItem().copy();
        if(entity.isCharging()) {
            if (entity.ticksToCharge <= 0) {
                stack.setDamage(stack.getDamage() - 1);
                entity.setItem(stack);
                if(!stack.isDamaged()) {
                    var sound = ModSounds.TERRITORY_COMPLETION;
                    if(stack.getItem() instanceof ElementalSwordItem) sound = ((ElementalSwordItem)stack.getItem()).element.completionSound;
                    if(stack.getItem() instanceof ElementalCoreItem) sound = ((ElementalCoreItem)stack.getItem()).element.completionSound;
                    world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1, 1);
                }
                entity.ticksToCharge = calculateTicksToCharge(world, pos);
            } else {
                entity.ticksToCharge--;
            }
            entity.updateListeners();
        }
    }



    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public ItemStack getItem() {
        return inventory.get(0);
    }

    public int getMaxCountPerStack() {
        return 1;
    }

    public boolean interact(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if(stackInHand.isEmpty()) {
            player.setStackInHand(hand, getItem());
            clear();
            return true;
        } else if (getItem().isEmpty()) {
            ItemStack newStack = stackInHand.split(1);
            player.setStackInHand(hand, stackInHand);
            setItem(newStack);
            return true;
        }
        return false;
    }

    public void clear() {
        getItems().clear();
        this.updateListeners();
    }

    void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack())
            stack.setCount(getMaxCountPerStack());
        this.updateListeners();
    }

    public void setItem(ItemStack stack) {
        setStack(0, stack);
        this.ticksToCharge = calculateTicksToCharge(world, pos);
    }

    private void updateListeners() {
        this.markDirty();
        this.sync();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public void sync() {
        if (world instanceof ServerWorld) ((ServerWorld)world).getChunkManager().markForUpdate(this.getPos());
    }

    public int size() {
        return 1;
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        writeNbt(nbtCompound);
        return nbtCompound;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory, true);
        nbt.putInt("TicksToCharge", ticksToCharge);

    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.clear();
        Inventories.readNbt(nbt, inventory);
        if (nbt.contains("TicksToCharge", NbtElement.INT_TYPE))
            ticksToCharge = nbt.getInt("TicksToCharge");
    }

    public boolean isCharging() {
        return getItem().isIn(ModTags.Items.ELEMENTAL_ITEMS) && getItem().isDamaged();
    }


}
