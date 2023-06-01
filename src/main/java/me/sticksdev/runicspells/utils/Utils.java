package me.sticksdev.runicspells.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Utils {
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
