package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.util.Element;

public class FlameElementalCoreItem extends ElementalCoreItem {


    public FlameElementalCoreItem(Settings settings) {
        super(settings, Element.FLAME,10,40);
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
                world.playSound(null, pos, stack.isDamaged() ? element.absorbSound : element.completionSound, SoundCategory.BLOCKS);
                user.getItemCooldownManager().set(this, absorbCooldown);
                return ActionResult.SUCCESS;
            }
        } else if (stack.getDamage() < stack.getMaxDamage()) {
            entity.setOnFireFor(useCooldown);
            entity.setAttacking(user);
            stack.setDamage(stack.getDamage() + 1);
            world.playSound(null, pos, element.extractSound, SoundCategory.BLOCKS);
            user.getItemCooldownManager().set(this, useCooldown);
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }
    
}
