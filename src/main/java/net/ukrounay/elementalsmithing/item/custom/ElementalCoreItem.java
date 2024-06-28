package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

    public final Element element;
    public final int absorbCooldown;
    public final int useCooldown;
    public final StatusEffect materialStatusEffect;
    public int terraformingStage;
    private int stageTicks = 10;

    public ElementalCoreItem(Settings settings, Element element, int absorbCooldown, int useCooldown, StatusEffect materialStatusEffect) {
        super(settings);
        this.element = element;
        this.absorbCooldown = absorbCooldown;
        this.useCooldown = useCooldown;
        this.materialStatusEffect = materialStatusEffect;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.fail(itemStack);
        Item item = itemStack.getItem();
        user.setCurrentHand(hand);
        float fullness = (float) (itemStack.getMaxDamage() - itemStack.getDamage()) /  itemStack.getMaxDamage();
        int usingAmplifier = (int)(10 * fullness) + 10;
        if(itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier && !user.getItemCooldownManager().isCoolingDown(item)) {
            user.addStatusEffect(new StatusEffectInstance(materialStatusEffect, useCooldown * usingAmplifier, (int)(4 * fullness)));
            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
            if(element.territory) {
                ShockwaveEffect.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                ElementalEnergyMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 0);
                terraformingStage = 0;
            }

        }
        return TypedActionResult.consume(itemStack);
    }

//    ItemStack itemStack = user.getStackInHand(hand);
//        if (CrossbowItem.isCharged(itemStack)) {
//        CrossbowItem.shootAll(world, user, hand, itemStack, CrossbowItem.getSpeed(itemStack), 1.0f);
//        CrossbowItem.setCharged(itemStack, false);
//        return TypedActionResult.consume(itemStack);
//    }
//        if (!user.getProjectileType(itemStack).isEmpty()) {
//        if (!CrossbowItem.isCharged(itemStack)) {
//            this.charged = false;
//            this.loaded = false;
//            user.setCurrentHand(hand);
//        }
//        return TypedActionResult.consume(itemStack);
//    }
//        return TypedActionResult.fail(itemStack);

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient() && element.territory) {
            ItemStack itemStack = user.getActiveItem();
            float fullness = (float) (itemStack.getMaxDamage() - itemStack.getDamage()) /  itemStack.getMaxDamage();
            int usingAmplifier = (int)(10 * fullness) + 10;
            if(ElementalEnergyMethods.isSpikesGenerating) {
                ElementalEnergyMethods.continueGeneratingSpikes(world);
            } else if(ElementalEnergyMethods.isRestoring) {
                ElementalEnergyMethods.continueRestoration(world);
            } else if(ElementalEnergyMethods.isTerraforming) {
                ElementalEnergyMethods.continueTerraforming(world);
            } else {
                switch (terraformingStage) {
                    case 0: case 1:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ElementalEnergyMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 0);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 2:
                        ShockwaveEffect.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                        ElementalEnergyMethods.startRestoration(stageTicks * 2);
                        itemStack.setDamage(Math.max(0, itemStack.getDamage() - usingAmplifier * 2));
                        terraformingStage++;
                        break;
                    case 3:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ElementalEnergyMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 1);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 4:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ElementalEnergyMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 2);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 5:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ShockwaveEffect.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                            ElementalEnergyMethods.prepareCurlySpikes(element.primaryBlock, element.secondaryBlock);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    default: break;
                }
            }

        }
    }
//    @Override
//    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
//        if (!world.isClient) {
//            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
//            SoundEvent soundEvent = this.getQuickChargeSound(i);
//            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
//            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float) CrossbowItem.getPullTime(stack);
//            if (f < 0.2f) {
//                this.charged = false;
//                this.loaded = false;
//            }
//            if (f >= 0.2f && !this.charged) {
//                this.charged = true;
//                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
//            }
//            if (f >= 0.5f && soundEvent2 != null && !this.loaded) {
//                this.loaded = true;
//                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
//            }
//        }
//    }


    //    @Override
//    public ActionResult useOnBlock(ItemUsageContext context) {
//        World world = context.getWorld();
//        if(world.isClient()) return ActionResult.CONSUME;
//        ItemStack stack = context.getStack();
//        BlockPos pos = context.getBlockPos();
//        if(List.of(element.convertBlock, element.terraformingStartBlock, element.terraformingFinishBlock)
//            .contains(world.getBlockState(pos).getBlock())
//        ) {
//            if(stack.isDamaged()) {
//                var breakSound = world.getBlockState(pos).getSoundGroup().getBreakSound();
//                world.removeBlock(pos, false);
//                stack.setDamage(stack.getDamage() - 1);
//                world.playSound(null, pos, stack.isDamaged() ? breakSound : element.completionSound, SoundCategory.BLOCKS);
//                context.getPlayer().getItemCooldownManager().set(stack.getItem(), absorbCooldown);
//                return ActionResult.SUCCESS;
//            }
//        } else if (element.convertBlock != null && stack.getDamage() < stack.getMaxDamage()) {
//            ElementalEnergyMethods.terraform(world, pos, element.terraformingStartBlock, 3);
//            stack.setDamage(stack.getDamage() + 1);
//            world.playSound(null, pos, element.extractSound, SoundCategory.BLOCKS);
//            context.getPlayer().getItemCooldownManager().set(stack.getItem(), useCooldown);
//            return ActionResult.SUCCESS;
//        }
//        return ActionResult.CONSUME;
//    }
//


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ElementalEnergyMethods.isTerraforming = false;
        ElementalEnergyMethods.isRestoring = false;
        ElementalEnergyMethods.isSpikesGenerating = false;
        ElementalEnergyMethods.restorationMap.clear();
        ElementalEnergyMethods.maxTerraformingDistance = 0;
        float fullness = (float) (stack.getMaxDamage() - stack.getDamage()) /  stack.getMaxDamage();
        int usingAmplifier = (int)(10 * fullness) + 10;
        ((PlayerEntity)user).getItemCooldownManager().set(stack.getItem(), useCooldown * usingAmplifier);
        user.addStatusEffect(new StatusEffectInstance(materialStatusEffect, useCooldown * usingAmplifier, (int)(4 * fullness)));
        world.playSound(null, user.getSteppingPos(), element.extractSound, SoundCategory.BLOCKS);
        return stack;
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ElementalEnergyMethods.isTerraforming = false;
        ElementalEnergyMethods.isRestoring = false;
        ElementalEnergyMethods.isSpikesGenerating = false;
        ElementalEnergyMethods.restorationMap.clear();
        ElementalEnergyMethods.maxTerraformingDistance = 0;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (element.territory) return 7 * (stageTicks + 1) + 33;
        return 0;
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
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.element")
            .append(": ")
            .append(Text.translatable("item." + tKey + ".element").formatted(element.formatting))
        );
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.energy")
            .append(": " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage())
        );
        tooltip.add(Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_core.hint" + (stack.isDamaged() ? "_damaged" : "")).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
