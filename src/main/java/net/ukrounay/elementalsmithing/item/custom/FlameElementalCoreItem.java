package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.sound.ModSounds;

import java.util.List;

public class FlameElementalCoreItem extends ElementalCoreItem {

    private static final List<Block> CONSUMABLE_BLOCKS = List.of(
        Blocks.FIRE,
        Blocks.SOUL_FIRE
    );


    public FlameElementalCoreItem(Settings settings) {
        super(settings, CONSUMABLE_BLOCKS, 0xFFFF0000,
            Formatting.RED, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundEvents.ITEM_FIRECHARGE_USE,
            ModSounds.FLAME_CORE_COMPLETION,10,10, StatusEffects.FIRE_RESISTANCE
        );
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if(world.isClient()) return ActionResult.CONSUME;
        BlockPos pos = entity.getBlockPos();
        if(entity.isOnFire()) {
            if(stack.isDamaged()) {
                entity.setOnFire(false);
                entity.setFireTicks(0);
                entity.setAttacking(null);
                stack.setDamage(stack.getDamage() - 1);
                world.playSound(null, pos, stack.isDamaged() ? absorbSound : completionSound, SoundCategory.BLOCKS);
                user.getItemCooldownManager().set(this, absorbCooldown);
                return ActionResult.SUCCESS;
            }
        } else if (stack.getDamage() < stack.getMaxDamage()) {
            entity.setOnFireFor(useCooldown);
            entity.setAttacking(user);
            stack.setDamage(stack.getDamage() + 1);
            world.playSound(null, pos, extractSound, SoundCategory.BLOCKS);
            user.getItemCooldownManager().set(this, useCooldown);
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }
    
}
