package me.sticksdev.runicspells.spells;
import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import me.sticksdev.runicspells.utils.Utils;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class EarthSpell extends ItemBasedSpell {
    public EarthSpell() {
        super("Earth", "Launches an earth projectile", 2, 10, "DIRT", 25, 40, 15, true, EarthSpell::castEarth);
    }

    private static void castEarth(Player player, Entity nearestEntity) {
        EarthSpell earthSpell = new EarthSpell();

        // Launches an earth projectile
        Projectile earth = player.launchProjectile(Snowball.class, Utils.getProjectileVelocity(player, nearestEntity));

        // Wait for it to hit something
        new BukkitRunnable() {
            @Override
            public void run() {
                if (earth.isDead() || earth.isOnGround()) {
                    // Get the location where the earth hit
                    Location impactLocation = earth.getLocation();

                    // Spawn earth around the impact location
                    World world = impactLocation.getWorld();
                    if (world != null) {
                        for (int x = -2; x <= 2; x++) {
                            for (int y = -2; y <= 2; y++) {
                                for (int z = -2; z <= 2; z++) {
                                    Location blockLocation = impactLocation.clone().add(x, y, z);
                                    Material blockType = blockLocation.getBlock().getType();

                                    // Check if the block is not air or bedrock
                                    if (blockType != Material.AIR && blockType != Material.BEDROCK) {
                                        TNTPrimed tnt = world.spawn(blockLocation, TNTPrimed.class);

                                        // Set the fuse ticks to 40 (2 seconds)
                                        tnt.setFuseTicks(40);
                                        tnt.setIsIncendiary(false);
                                    }
                                }
                            }
                        }
                    }

                    if (nearestEntity instanceof LivingEntity LE) {
                        // Damage the nearest entity
                        LE.damage(earthSpell.getDamage());
                    }

                    // Cancel the task
                    cancel();
                }
            }
        }.runTaskTimer(Runic_spells.getInstance(), 0L, 1L);
    }
}
