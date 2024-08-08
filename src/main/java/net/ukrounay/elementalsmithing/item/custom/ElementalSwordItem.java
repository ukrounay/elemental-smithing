package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.ElementalSmithing;
import net.ukrounay.elementalsmithing.util.Element;
import net.ukrounay.elementalsmithing.item.ModToolMaterials;
import net.ukrounay.elementalsmithing.sound.ModSounds;
import net.ukrounay.elementalsmithing.util.FastMath;
import net.ukrounay.elementalsmithing.util.ModTerraformingMethods;
import net.ukrounay.elementalsmithing.util.ModEntitiesInteractions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElementalSwordItem extends SwordItem {

    public final Element element;
    private final Enchantment enchantment;
    private final int bonusDamage;
    public final int useCooldown;
    public int terraformingStage;
    private int stageTicks = 10;

    public ElementalSwordItem(Element element, int attackDamage, int bonusDamage, float attackSpeed, int useCooldown, Enchantment enchantment, Settings settings) {
        super(ModToolMaterials.ELEMENTAL, attackDamage, attackSpeed, settings);
        this.element = element;
        this.enchantment = enchantment;
        this.bonusDamage = bonusDamage;
        this.useCooldown = useCooldown;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return element.color;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.clear();
        String tKey = Registries.ITEM.getId(this).toTranslationKey();
        tooltip.add(Text.translatable("item." + tKey).formatted(element.formatting));
        tooltip.add(Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.territory_hint" + (element.territory ? "" : "_absent"))
                        .formatted(Formatting.GRAY));
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.element")
            .append(": ")
            .append(Text.translatable(element.getIdentifier()).formatted(element.formatting))
        );
        tooltip.add(
            Text.translatable("tooltip." + ElementalSmithing.MOD_ID + ".elemental_item.energy")
            .append(": " + (stack.getMaxDamage() - stack.getDamage()) + "/" + stack.getMaxDamage())
        );
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float fullness =  1 - stack.getDamage() /  (float)stack.getMaxDamage();
        int damage = (int)(bonusDamage * fullness);
        if (stack.getDamage() + damage < stack.getMaxDamage() && attacker instanceof PlayerEntity) {
            target.damage(target.getDamageSources().playerAttack((PlayerEntity)attacker), damage);
            stack.setDamage(stack.getDamage() + damage);
        }
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        if (element.territory) return 7 * (stageTicks + 1) + 35;
        return 0;
    }



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.fail(itemStack);
        Item item = itemStack.getItem();
        user.setCurrentHand(hand);
        float fullness = (float) (itemStack.getMaxDamage() - itemStack.getDamage()) /  itemStack.getMaxDamage();
        int usingAmplifier = (int)(useCooldown * fullness) + useCooldown;
        if(itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier && !user.getItemCooldownManager().isCoolingDown(item)) {
            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
            if(element.territory) {
                ModEntitiesInteractions.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                ModTerraformingMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 0);
                terraformingStage = 0;
            } else {
                user.addStatusEffect(new StatusEffectInstance(element.statusEffect, usingAmplifier, Math.round(4f * fullness)));
                if(element.extractSound != null)
                    world.playSound(null, user.getBlockPos(), element.extractSound, SoundCategory.BLOCKS);
                user.getItemCooldownManager().set(itemStack.getItem(), usingAmplifier);
            }
        }
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient() && element.territory) {
            ItemStack itemStack = user.getActiveItem();
            float fullness = (float) (itemStack.getMaxDamage() - itemStack.getDamage()) /  itemStack.getMaxDamage();
            int usingAmplifier = (int)(10 * fullness) + 10;
            if(ModTerraformingMethods.isSpikesGenerating) {
                ModTerraformingMethods.continueGeneratingSpikes(world);
            } else if(ModTerraformingMethods.isRestoring) {
                ModTerraformingMethods.continueRestoration(world);
            } else if(ModTerraformingMethods.isTerraforming) {
                ModTerraformingMethods.continueTerraforming(world);
            } else {
                switch (terraformingStage) {
                    case 0: case 1:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ModTerraformingMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 0);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 2:
                        ModEntitiesInteractions.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                        ModTerraformingMethods.startRestoration(world, stageTicks * 2);
                        itemStack.setDamage(Math.max(0, itemStack.getDamage() - usingAmplifier * 2));
                        terraformingStage++;
                        break;
                    case 3:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ModTerraformingMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 1);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 4:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ModTerraformingMethods.startTerraforming(world, user.getSteppingPos(), element.primaryBlock, element.secondaryBlock, stageTicks, 2);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    case 5:
                        if (itemStack.getDamage() <= itemStack.getMaxDamage() - usingAmplifier) {
                            ModEntitiesInteractions.createShockwave(world, user.getSteppingPos(), 10, 2, user);
                            ModTerraformingMethods.prepareCurlySpikes(world, element.primaryBlock, element.secondaryBlock);
                            itemStack.setDamage(itemStack.getDamage() + usingAmplifier);
                        }
                        terraformingStage++;
                        break;
                    default: break;
                }
            }
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ModTerraformingMethods.isTerraforming = false;
        ModTerraformingMethods.isRestoring = false;
        ModTerraformingMethods.isSpikesGenerating = false;
        ModTerraformingMethods.restorationMap.clear();
        ModTerraformingMethods.maxTerraformingDistance = 0;
        if(element.territory) {
            float fullness = (float) (stack.getMaxDamage() - stack.getDamage()) /  stack.getMaxDamage();
            int usingAmplifier = (int)(10 * fullness) + 10;
            ((PlayerEntity)user).getItemCooldownManager().set(stack.getItem(), useCooldown * usingAmplifier);
            user.addStatusEffect(new StatusEffectInstance(element.statusEffect, useCooldown * usingAmplifier, (int)(4 * fullness)));
            world.playSound(null, user.getSteppingPos(), ModSounds.TERRITORY_COMPLETION, SoundCategory.BLOCKS);
        }
        return stack;
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ModTerraformingMethods.isTerraforming = false;
        ModTerraformingMethods.isRestoring = false;
        ModTerraformingMethods.isSpikesGenerating = false;
        ModTerraformingMethods.restorationMap.clear();
        ModTerraformingMethods.maxTerraformingDistance = 0;
        if(element.territory) {
            ((PlayerEntity)user).getItemCooldownManager().set(stack.getItem(), useCooldown / 2);
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        stack.addEnchantment(enchantment, enchantment.getMaxLevel());
        return stack;
    }
}
