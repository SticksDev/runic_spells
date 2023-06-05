package me.sticksdev.runicspells.handlers;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import me.sticksdev.runicspells.utils.Redis;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.SetParams;

/**
 * Handles cooldowns
 */
public class CooldownHandler {
    private final Redis redis = Runic_spells.getInstance().getRedisHandler();
    private final JedisPooled pool = redis.getPool();

    // Note: Java doesn't support union types for parameters, so we explicitly define the type of the spell
    // Even though it *technically* could be a BaseSpell, it's not worth the hassle of casting it

    /**
     * Sets the cooldown for a player
     *
     * @param player   The player to set the cooldown for
     * @param spell    The spell to set the cooldown for (type ItemBasedSpell)
     * @param cooldown The cooldown to set (in seconds)
     */
    public void setCooldown(Player player, ItemBasedSpell spell, int cooldown) {
        pool.set(player.getUniqueId() + ":" + spell.getSpellID() + ":cooldown", String.valueOf(cooldown), new SetParams().ex(cooldown));
    }


    /**
     * Gets the cooldown for a player
     *
     * @param player The player to get the cooldown for
     * @param spell  The spell to get the cooldown for (type ItemBasedSpell)
     * @return The cooldown for the player (in seconds)
     */
    public double getCooldown(Player player, ItemBasedSpell spell) {
        String key = player.getUniqueId() + ":" + spell.getSpellID() + ":cooldown";
        String cooldown = pool.get(key);

        // Check if the key exists in Redis
        if (cooldown == null) {
            return 0; // No cooldown
        }

        // Get the remaining time to live in seconds
        long ttl = pool.ttl(key);

        // Check if the object has expired
        if (ttl < 0) {
            return 0; // Object has expired, so no cooldown
        }

        // Return the time left before the object is gone
        return (double) ttl;
    }
}
