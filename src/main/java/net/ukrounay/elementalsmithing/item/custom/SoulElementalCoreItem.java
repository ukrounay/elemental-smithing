package net.ukrounay.elementalsmithing.item.custom;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Pair;
import net.ukrounay.elementalsmithing.damage.ModDamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ukrounay.elementalsmithing.util.Element;
import net.ukrounay.elementalsmithing.item.ModItems;

import java.lang.reflect.Type;
import java.util.List;


public class SoulElementalCoreItem extends ElementalCoreItem {

    public static final List<Pair<Type, ItemStack>> DROPS = List.of(
        new Pair<>(FrogEntity.class, ModItems.TEGUKJFLK_MUSIC_DISK.getDefaultStack()),
        new Pair<>(SquidEntity.class, ModItems.IWSEUVHJK_MUSIC_DISK.getDefaultStack()),
        new Pair<>(CowEntity.class, ModItems.ZSDXGHJMR_MUSIC_DISK.getDefaultStack()),
        new Pair<>(RabbitEntity.class, ModItems.EZSXDFCGH_MUSIC_DISK.getDefaultStack()),
        new Pair<>(BatEntity.class, ModItems.UBDCWJNK_MUSIC_DISK.getDefaultStack())
    );

    public SoulElementalCoreItem(Settings settings) {
        super(settings, Element.SOUL, 30,60);
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

            if(entity.isDead())
                for(Pair<Type, ItemStack> pair : DROPS)
                    if (entity.getClass() == pair.getLeft())
                        dropCustomLoot(world, pos, pair.getRight());

            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
    }

    /**
     * Drops custom loot at the given position.
     *
     * @param world The game world.
     * @param pos The position to drop the custom loot.
     * @param customLoot The loot that will be dropped.
     */
    private void dropCustomLoot(World world, BlockPos pos, ItemStack customLoot) {
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), customLoot);
        world.spawnEntity(itemEntity);
    }

}
