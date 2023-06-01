package me.sticksdev.runicspells.spells;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import me.sticksdev.runicspells.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireSpell extends ItemBasedSpell {
    public FireSpell() {
        super("Fireball", "Launches a fireball projectile", 1,  "FIRE_CHARGE", 6, 25, 15, FireSpell::castFireball);
    }

    private static void castFireball(Player player, Entity nearestEntity) {
        // Launches a fireball projectile
        Projectile fireball = player.launchProjectile(Snowball.class, Utils.getProjectileVelocity(player, nearestEntity));

        // Set the fireball's damage
        fireball.setFireTicks(100);

        // Wait for it to hit something
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireball.isDead() || fireball.isOnGround()) {
                    // Get the location where the fireball hit
                    Location impactLocation = fireball.getLocation();

                    // Spawn fire around the impact location
                    World world = impactLocation.getWorld();
                    if (world != null) {
                        for (int x = -2; x <= 2; x++) {
                            for (int y = -2; y <= 2; y++) {
                                for (int z = -2; z <= 2; z++) {
                                    Location fireLocation = impactLocation.clone().add(x, y, z);
                                    if (fireLocation.getBlock().getType() == Material.AIR) {
                                        fireLocation.getBlock().setType(Material.FIRE);
                                    }

                                    // Add explosion effect
                                    world.createExplosion(fireLocation, 0.0F, false);
                                }
                            }
                        }
                    }

                    // Cancel the task
                    cancel();
                }
            }
        }.runTaskTimer(Runic_spells.getInstance(), 0L, 1L);
    }
}
