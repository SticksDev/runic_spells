package me.sticksdev.runicspells.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Simple Utils class for repeated code or tasks.
 */
public class Utils {
    /**
     * Gets the velocity of a projectile based on the player's location and the target's location.
     *
     * @param player The player to get the velocity from.
     * @param target The target to get the velocity to.
     * @return The velocity of the projectile.
     * @see org.bukkit.util.Vector Vector
     */
    public static Vector getProjectileVelocity(Player player, Entity target) {
        Vector direction;
        if (target != null) {
            direction = target.getLocation().toVector().subtract(player.getLocation().toVector());
        } else {
            direction = player.getEyeLocation().getDirection();
        }
        return direction.normalize().multiply(1.5);
    }
}
