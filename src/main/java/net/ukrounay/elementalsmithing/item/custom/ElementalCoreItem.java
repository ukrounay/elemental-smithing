package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.util.Element;
import net.ukrounay.elementalsmithing.util.ModEntitiesInteractions;
import org.jetbrains.annotations.Nullable;
import java.util.List;


public class ElementalCoreItem extends Item {

    public final Element element;
    public final int absorbCooldown;
    public final int useCooldown;

    public ElementalCoreItem(Settings settings, Element element, int absorbCooldown, int useCooldown) {
        super(settings);
        this.element = element;
        this.absorbCooldown = absorbCooldown;
        this.useCooldown = useCooldown;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient() || !user.isSneaking()) return TypedActionResult.pass(stack);
        Item item = stack.getItem();
        float fullness = 1 - (float)stack.getDamage() /  stack.getMaxDamage();
        int usingAmplifier = (int)(useCooldown * fullness) + useCooldown;
        int ticks = (useCooldown + usingAmplifier) * 4;
        if(stack.getDamage() <= stack.getMaxDamage() - usingAmplifier && !user.getItemCooldownManager().isCoolingDown(item)) {
            user.addStatusEffect(new StatusEffectInstance(element.statusEffect, ticks));
            stack.setDamage(stack.getDamage() + usingAmplifier);
            ModEntitiesInteractions.createShockwave(world, user.getSteppingPos(), 5, 1, user);
            world.playSound(null, user.getBlockPos(), element.extractSound, SoundCategory.BLOCKS);
            user.getItemCooldownManager().set(stack.getItem(), ticks);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if(world.isClient() || !element.territory) return ActionResult.CONSUME;
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        if(player.isSneaking()) {
            use(world, player, context.getHand());
            return ActionResult.CONSUME;
        }
        if(List.of(element.convertBlock, element.primaryBlock, element.secondaryBlock)
                .contains(world.getBlockState(pos).getBlock())
        ) {
            if(stack.isDamaged()) {
                world.removeBlock(pos, false);
                stack.setDamage(stack.getDamage() - 1);
                var breakSound = world.getBlockState(pos).getSoundGroup().getBreakSound();
                world.playSound(null, pos, stack.isDamaged() ? breakSound : element.completionSound, SoundCategory.BLOCKS);
                player.getItemCooldownManager().set(stack.getItem(), absorbCooldown);
                return ActionResult.SUCCESS;
            }
        } else if (element.convertBlock != null &&
                stack.getDamage() < stack.getMaxDamage() &&
                context.getSide() == Direction.UP &&
                world.canSetBlock(pos.up()) &&
                world.getBlockState(pos.up()).isReplaceable()
        ) {
            world.setBlockState(pos.up(), element.convertBlock.getDefaultState());
            stack.setDamage(stack.getDamage() + 1);
            world.playSound(null, pos, element.extractSound, SoundCategory.BLOCKS);
            player.getItemCooldownManager().set(stack.getItem(), useCooldown);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }


    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        stack.setDamage(stack.getMaxDamage());
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return element.color;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        String tKey = Registries.ITEM.getId(this).toTranslationKey();
        tooltip.add(Text.translatable("item." + tKey).formatted(element.formatting));
        tooltip.add(Text.translatable("item." + tKey + ".description").formatted(Formatting.GRAY));
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.element")
            .append(": ")
            .append(Text.translatable(element.getIdentifier()).formatted(element.formatting))
        );
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.energy")
            .append(": " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage())
        );
        tooltip.add(Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.hint" + (stack.isDamaged() ? "_damaged" : "")).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
