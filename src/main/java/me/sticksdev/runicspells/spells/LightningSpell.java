package me.sticksdev.runicspells.spells;


import me.sticksdev.runicspells.structures.ItemBasedSpell;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LightningSpell extends ItemBasedSpell {
    public LightningSpell() {
        super("Lightning", "Strikes lightning at the target", 4, 25, "BLAZE_ROD", 3, 10, 10, true, LightningSpell::castLightning);
    }

    private static void castLightning(Player player, Entity nearestEntity) {
        LightningSpell lightningSpell = new LightningSpell();

        World world = nearestEntity.getLocation().getWorld();
        Location location = nearestEntity.getLocation();

        world.strikeLightningEffect(location);

        if (nearestEntity instanceof LivingEntity LE) {
            LE.damage(lightningSpell.getDamage());
        }
    }
}
