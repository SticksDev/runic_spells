package me.sticksdev.runicspells.spells;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import me.sticksdev.runicspells.utils.Utils;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WaterSpell extends ItemBasedSpell {
    public WaterSpell() {
        super("Water", "Launches a water projectile", 3, 15, "WATER_BUCKET", 3, 10, 0, true, WaterSpell::castWater);
    }

    private static void castWater(Player player, Entity nearestEntity) {
        // Launches a water projectile
        Projectile water = player.launchProjectile(Snowball.class, Utils.getProjectileVelocity(player, nearestEntity));
        WaterSpell waterSpell = new WaterSpell();

        // Wait for it to hit something
        new BukkitRunnable() {
            @Override
            public void run() {
                if (water.isDead() || water.isOnGround()) {
                    // Get the location where the water hit
                    Location impactLocation = water.getLocation();

                    // Spawn water around the impact location
                    World world = impactLocation.getWorld();
                    if (world != null) {
                        for (int x = -2; x <= 2; x++) {
                            for (int y = -2; y <= 2; y++) {
                                for (int z = -2; z <= 2; z++) {
                                    Location waterLocation = impactLocation.clone().add(x, y, z);
                                    if (waterLocation.getBlock().getType() == Material.AIR) {
                                        waterLocation.getBlock().setType(Material.WATER);
                                    }

                                    // Add explosion effect
                                    world.createExplosion(waterLocation, 0.0F, false);
                                }
                            }
                        }
                    }

                    if (nearestEntity instanceof LivingEntity LE) {
                        // Damage the nearest entity
                        LE.damage(waterSpell.getDamage());
                    }

                    // Cancel the task
                    cancel();
                }
            }
        }.runTaskTimer(Runic_spells.getInstance(), 0L, 1L);
    }
}
