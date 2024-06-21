package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import org.jetbrains.annotations.Nullable;
import java.util.List;


public class ElementalCoreItem extends Item {

    public final List<Block> consumableBlocks;
    public final int color;

    public SoundEvent extractSound;
    public SoundEvent absorbSound;
    public final SoundEvent completionSound;
    public Formatting formatting;
    public int absorbCooldown;
    public int useCooldown;

    public StatusEffect materialStatusEffect;

    public ElementalCoreItem(
        Settings settings, List<Block> consumableBlocks, int color, Formatting formatting,
        SoundEvent absorbSound, SoundEvent extractSound, SoundEvent completionSound, int absorbCooldown, int useCooldown,
        StatusEffect statusEffectType
    ) {
        super(settings);
        this.consumableBlocks = consumableBlocks;
        this.color = color;
        this.formatting = formatting;
        this.absorbSound = absorbSound;
        this.extractSound = extractSound;
        this.completionSound = completionSound;
        this.absorbCooldown = absorbCooldown;
        this.useCooldown = useCooldown;
        this.materialStatusEffect = statusEffectType;
    }
    public ElementalCoreItem(Settings settings, int color, Formatting formatting, SoundEvent absorbSound, SoundEvent extractSound, SoundEvent completionSound, int absorbCooldown, int useCooldown, StatusEffect statusEffectType) {
        this(settings, List.of(), color, formatting, absorbSound, extractSound, completionSound, absorbCooldown, useCooldown, statusEffectType);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Item item = itemStack.getItem();
        float fullness = (itemStack.getMaxDamage() - itemStack.getDamage()) /  itemStack.getMaxDamage();
        int usingAmplifier = (int)(10 * fullness) + 10;
        if(!world.isClient() && itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier && !user.getItemCooldownManager().isCoolingDown(item)) {
            user.addStatusEffect(new StatusEffectInstance(materialStatusEffect, useCooldown * usingAmplifier, (int)(4 * fullness)));
            user.getItemCooldownManager().set(item, useCooldown * usingAmplifier);
            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
            world.playSound(null, user.getBlockPos(), extractSound, SoundCategory.BLOCKS);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(world.isClient()) return ActionResult.CONSUME;
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        if(!consumableBlocks.isEmpty() && consumableBlocks.contains(world.getBlockState(pos).getBlock())) {
            if(stack.isDamaged()) {
                world.removeBlock(pos, false);
                stack.setDamage(stack.getDamage() - 1);
                world.playSound(null, pos, stack.isDamaged() ? absorbSound : completionSound, SoundCategory.BLOCKS);
                context.getPlayer().getItemCooldownManager().set(stack.getItem(), absorbCooldown);
                return ActionResult.SUCCESS;
            }
        } else if (!consumableBlocks.isEmpty() && stack.getDamage() < stack.getMaxDamage() && world.getBlockState(pos.up()).isReplaceable()) {
            world.setBlockState(pos.up(), consumableBlocks.get(0).getDefaultState());
            stack.setDamage(stack.getDamage() + 1);
            world.playSound(null, pos, extractSound, SoundCategory.BLOCKS);
            context.getPlayer().getItemCooldownManager().set(stack.getItem(), useCooldown);
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        stack.setDamage(stack.getMaxDamage());
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return color;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        String tKey = Registries.ITEM.getId(this).toTranslationKey();
        tooltip.add(Text.translatable("item." + tKey).formatted(formatting));
        tooltip.add(Text.translatable("item." + tKey + ".description").formatted(Formatting.GRAY));
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.element")
            .append(": ")
            .append(Text.translatable("item." + tKey + ".element").formatted(formatting))
        );
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.energy")
            .append(": " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage())
        );
        tooltip.add(Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.hint" + (stack.isDamaged() ? "_damaged" : "")).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
