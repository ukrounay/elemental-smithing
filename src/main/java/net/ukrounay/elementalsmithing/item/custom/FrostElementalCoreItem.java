package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.sound.ModSounds;

import java.awt.*;
import java.util.List;

public class FrostElementalCoreItem extends ElementalCoreItem {


    private static final List<Block> CONSUMABLE_BLOCKS = List.of(
        Blocks.SNOW,
        Blocks.SNOW_BLOCK,
        Blocks.POWDER_SNOW
    );

    public FrostElementalCoreItem(Settings settings) {
        super(settings, CONSUMABLE_BLOCKS, 0xFF55FFFF,
                Formatting.AQUA, SoundEvents.BLOCK_SNOW_BREAK, SoundEvents.BLOCK_SNOW_PLACE,
                ModSounds.FROST_CORE_COMPLETION, 20,20, StatusEffects.SLOW_FALLING
        );
    }

//    @Override
//    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
//        World world = user.getWorld();
//        if(world.isClient()) return ActionResult.SUCCESS;
//        BlockPos pos = entity.getBlockPos();
//        if(entity.isFrozen()) {
//            if(stack.isDamaged()) {
//                entity.setFrozenTicks(0);
//                stack.setDamage(stack.getDamage() - 1);
//                world.playSound(null, pos, absorbSound, SoundCategory.BLOCKS);
//            }
//        } else if (stack.getDamage() < stack.getMaxDamage()) {
//            entity.setFrozenTicks(50);
//            stack.setDamage(stack.getDamage() + 1);
//            world.playSound(null, pos, extractSound, SoundCategory.BLOCKS);
//        }
//        return ActionResult.SUCCESS;
//    }

}
