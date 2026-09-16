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
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.block.custom.EnergyCondensatorBlock;
import net.ukrounay.elementalsmithing.item.custom.ElementalCoreItem;
import net.ukrounay.elementalsmithing.item.custom.ElementalSwordItem;
import net.ukrounay.elementalsmithing.sound.ModSounds;
import net.ukrounay.elementalsmithing.util.ModTags;

import java.util.ArrayList;
import java.util.List;

public class EnergyCondensatorBlockEntity extends BlockEntity {

    public static final int maxTicksToCharge = 72;

    public int ticksToCharge = 0;
    public int portalTicks = 0;
    public Text cachedText = Text.of("?");

    // Per-tick cache, refreshed at the top of every tick() call — NOT persisted, NOT a source
    // of truth. Ownership is always derived fresh from BlockPos comparison; this field only
    // exists so the renderer (called every frame, more often than tick()) can read a cheap
    // boolean instead of re-running findArrayPartners() itself.
    private boolean cachedIsOwner = true;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
    private int countcount = 0;
    private final ArrayList<Integer> counts = new ArrayList<>();

    public EnergyCondensatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_CONDENSATOR, pos, state);
    }

    // ---------- ticking ----------

    public static void tick(World world, BlockPos pos, BlockState blockState, EnergyCondensatorBlockEntity entity) {
        List<EnergyCondensatorBlockEntity> partners = entity.findArrayPartners();
        entity.cachedIsOwner = entity.resolveOwner(partners) == entity;

        int oldTicksToCharge = entity.ticksToCharge;

        if (entity.cachedIsOwner && entity.isCharging()) {
            entity.tickCharging(world, pos, blockState);
        }

        if (!entity.cachedIsOwner && !entity.inventory.isEmpty()) {
            ItemScatterer.spawn(world, pos, entity.inventory);
            entity.inventory.clear();
        }

        if (oldTicksToCharge != entity.ticksToCharge) {
            entity.cachedText = Text.of(String.valueOf(entity.ticksToCharge));
            for (EnergyCondensatorBlockEntity partner : partners) {
                partner.ticksToCharge = entity.ticksToCharge;
                partner.cachedText = entity.cachedText;
            }
        }
        entity.counts.add(entity.countcount);
        entity.countcount = 0;
        if (entity.counts.size() > 10) {
            String logstring = "Neighbours counted count last 10 ticks: ";
            for (int count : entity.counts) {
                logstring = logstring.concat(String.valueOf(count)).concat(", ");
            }
            ElementalSmithing.LOGGER.info(logstring);
            entity.counts.clear();
        }
    }

    private void tickCharging(World world, BlockPos pos, BlockState blockState) {
        ItemStack stack = getItem().copy();
        if (ticksToCharge <= 0) {
            stack.setDamage(stack.getDamage() - 1);
            setItem(stack);
            if (!stack.isDamaged()) {
                world.playSound(null, pos, resolveCompletionSound(stack), SoundCategory.BLOCKS, 1, 1);
            }
            ticksToCharge = calculateTicksToCharge(world, pos, blockState, this);
        } else {
            ticksToCharge--;
        }
        updateListeners();
    }

    private SoundEvent resolveCompletionSound(ItemStack stack) {
        if (stack.getItem() instanceof ElementalSwordItem esi) return esi.element.completionSound;
        if (stack.getItem() instanceof ElementalCoreItem eci) return eci.element.completionSound;
        return ModSounds.TERRITORY_COMPLETION;
    }

    public static int calculateTicksToCharge(World world, BlockPos pos, BlockState blockState, EnergyCondensatorBlockEntity entity) {
        return (int) (maxTicksToCharge / entity.getEfficiencyMultiplier(world, pos, blockState));
    }

    // ---------- array / ownership ----------

    private BlockPos getTargetPos() {
        Direction facing = getCachedState().get(EnergyCondensatorBlock.FACING);
        return pos.offset(facing);
    }

    private List<EnergyCondensatorBlockEntity> findArrayPartners() {
        List<EnergyCondensatorBlockEntity> partners = new ArrayList<>();
        if (world == null) return partners;

        BlockPos target = getTargetPos();
        for (Direction dir : Direction.values()) {
            BlockPos candidatePos = target.offset(dir.getOpposite());
            if (candidatePos.equals(this.pos)) continue;

            if (world.getBlockEntity(candidatePos) instanceof EnergyCondensatorBlockEntity other
                    && other.getCachedState().get(EnergyCondensatorBlock.FACING) == dir
                    && other.getTargetPos().equals(target)) {
                partners.add(other);
            }
        }
        return partners;
    }

    public List<EnergyCondensatorBlockEntity> getArrayPartners() {
        countcount++;
        return findArrayPartners();
    }

    private EnergyCondensatorBlockEntity resolveOwner(List<EnergyCondensatorBlockEntity> partners) {
        EnergyCondensatorBlockEntity owner = this;
        for (EnergyCondensatorBlockEntity partner : partners) {
            if (partner.pos.compareTo(owner.pos) < 0) owner = partner;
        }
        return owner;
    }

    public EnergyCondensatorBlockEntity getStorageOwner() {
        return resolveOwner(getArrayPartners());
    }

    /** Cheap per-tick-cached read — safe to call every render frame, unlike getStorageOwner(). */
    public boolean isStorageOwner() {
        return cachedIsOwner;
    }

    // ---------- inventory ----------

    public int size() {
        return 1;
    }

    public int getMaxCountPerStack() {
        return 1;
    }

    public DefaultedList<ItemStack> getItems() {
        return getStorageOwner().inventory;
    }

    public ItemStack getItem() {
        return getItems().get(0);
    }

    public void setItem(ItemStack stack) {
        setStack(0, stack);
    }

    void setStack(int slot, ItemStack stack) {
        if (stack.getCount() > getMaxCountPerStack()) stack.setCount(getMaxCountPerStack());
        EnergyCondensatorBlockEntity storageOwner = getStorageOwner();
        storageOwner.inventory.set(slot, stack);
        storageOwner.updateListeners();
    }

    public void clear() {
        EnergyCondensatorBlockEntity storageOwner = getStorageOwner();
        storageOwner.inventory.clear();
        storageOwner.updateListeners();
    }

    public boolean interact(PlayerEntity player, Hand hand) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if (stackInHand.isEmpty()) {
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

    public boolean isCharging() {
        return getItem().isIn(ModTags.Items.ENERGY_REPAIRABLE) && getItem().isDamaged();
    }

    private float getEfficiencyMultiplier(World world, BlockPos pos, BlockState blockState) {
        float multiplier = EnergyCondensatorBlock.getPower(world, pos, blockState);
        for (EnergyCondensatorBlockEntity partner : getArrayPartners()) {
            multiplier += EnergyCondensatorBlock.getPower(world, partner.getPos());
        }
        return multiplier;
    }

    // ---------- listeners / sync ----------

    private void updateListeners() {
        markDirty();
        sync();
        if (world != null) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public void sync() {
        if (world instanceof ServerWorld sw) sw.getChunkManager().markForUpdate(pos);
    }

    // ---------- persistence ----------

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
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
        if (nbt.contains("TicksToCharge", NbtElement.INT_TYPE)) {
            ticksToCharge = nbt.getInt("TicksToCharge");
        }
    }
}