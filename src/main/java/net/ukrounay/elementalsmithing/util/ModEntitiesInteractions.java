package net.ukrounay.elementalsmithing.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ModEntitiesInteractions {

    /**
     * Initiates a shockwave from the specified position.
     *
     * @param world The game world.
     * @param origin The origin position of the shockwave.
     * @param radius The radius of the shockwave.
     * @param power The power of the knockback force.
     */
    public static void createShockwave(World world, BlockPos origin, double radius, double power, LivingEntity except) {
        List<Entity> entities = world.getOtherEntities(except, new Box(
                origin.getX() - radius, origin.getY() - radius, origin.getZ() - radius,
                origin.getX() + radius, origin.getY() + radius, origin.getZ() + radius
        ));
        for (Entity entity : entities) {
            double distance = entity.getPos().distanceTo(Vec3d.ofCenter(origin));
            if (distance <= radius && entity instanceof LivingEntity) {
                Vec3d direction = entity.getPos().subtract(Vec3d.ofCenter(origin)).normalize();
                double knockbackStrength = power * (1 - (distance / radius));
                applyKnockback(entity, direction, knockbackStrength);
            }
        }
    }

    /**
     * Applies a knockback force to the specified entity.
     *
     * @param entity The entity to apply the knockback to.
     * @param direction The direction of the knockback.
     * @param strength The strength of the knockback.
     */
    private static void applyKnockback(Entity entity, Vec3d direction, double strength) {
        Vec3d knockbackVelocity = direction.multiply(strength);
        entity.addVelocity(knockbackVelocity.x, knockbackVelocity.y, knockbackVelocity.z);
        entity.velocityModified = true;
    }
}
