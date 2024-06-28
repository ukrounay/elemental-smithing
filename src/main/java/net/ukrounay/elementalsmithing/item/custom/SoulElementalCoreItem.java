package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.ukrounay.elementalsmithing.damage.ModDamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SoulElementalCoreItem extends ElementalCoreItem {

    public SoulElementalCoreItem(Settings settings) {
        super(settings, Element.SOUL, 30,30, StatusEffects.REGENERATION
        );
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if(world.isClient()) return ActionResult.CONSUME;
        BlockPos pos = entity.getBlockPos();
        float entityHealth = entity.getMaxHealth();
        int damageAmount = 1;
        if(entityHealth > 100) damageAmount = 5 + (int)(entityHealth / 50);
        else if(entityHealth > 20) damageAmount = Math.max(1, (int)(entityHealth / 20));

        if (stack.isDamaged() && !user.getItemCooldownManager().isCoolingDown(this)) {
            entity.damage(ModDamageTypes.of(world, ModDamageTypes.VITAL_ENERGY_DRAIN_DAMAGE_TYPE), damageAmount);
            stack.setDamage(Math.max(0, stack.getDamage() - damageAmount));
            world.playSound(null, pos, stack.isDamaged() ? element.absorbSound : element.completionSound, SoundCategory.BLOCKS);
            user.getItemCooldownManager().set(this, absorbCooldown);
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

}
