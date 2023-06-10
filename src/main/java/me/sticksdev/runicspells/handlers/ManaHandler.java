package me.sticksdev.runicspells.handlers;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.utils.Redis;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.JedisPooled;

/**
 * Handles mana
 */
public class ManaHandler implements Listener {
    private final Redis redis = Runic_spells.getInstance().getRedisHandler();
    private final JedisPooled pool = redis.getPool();
    private BukkitRunnable manaTimer;

    /**
     * Sets the mana of a player
     *
     * @param uuid The UUID of the player
     * @param mana The amount of mana to set
     */
    public void setMana(String uuid, int mana) {
        pool.set(uuid + ":mana", String.valueOf(mana));
    }

    /**
     * Creates the main BukkitRunnable for the mana timer to regenerate mana
     * This is called in the onEnable() method of the main class
     */
    public void startTimers() {
        manaTimer = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Runic_spells.getInstance().getServer().getOnlinePlayers()) {
                    int mana = getMana(player.getUniqueId().toString());
                    if (mana < 100) {
                        // Give an amount between 1 and 5
                        int amount = (int) (Math.random() * 5 + 1);
                        int newMana = mana + amount;

                        // Ensure they don't go over 100
                        if (newMana > 100) {
                            setMana(player.getUniqueId().toString(), 100);
                        } else {
                            addMana(player, amount);
                        }

                        // If the new mana is either equal or greater than 100, send a message to the player
                        if (newMana >= 100) {
                            // Send a message to the player saying they're at max mana
                            final Component message = Component.text()
                                    .append(Component.text("[RS]", NamedTextColor.GREEN, TextDecoration.BOLD))
                                    .append(Component.text(" You are at max mana!", NamedTextColor.GRAY))
                                    .build();
                            player.sendMessage(message);
                        }
                    }
                }
            }
        };
        manaTimer.runTaskTimer(Runic_spells.getInstance(), 0, 20);
    }

    /**
     * Cancels the mana timer
     * This is called in the onDisable() method of the main class
     */
    public void destroyTimers() {
        manaTimer.cancel();
    }

    /**
     * Gets the mana of a player
     *
     * @param uuid The UUID of the player
     * @return The amount of mana the player has
     */
    public int getMana(String uuid) {
        String mana = pool.get(uuid + ":mana");

        if (mana == null) {
            return 0;
        }

        return Integer.parseInt(mana);
    }

    /**
     * Adds mana to a player and updates their EXP bar
     *
     * @param player The player to add mana to
     * @param mana   The amount of mana to add
     */
    public void addMana(Player player, int mana) {
        String uuid = player.getUniqueId().toString();
        int currentMana = getMana(uuid);
        setMana(uuid, currentMana + mana);
        setEXPBar(player);
    }

    /**
     * Removes mana from a player and updates their EXP bar
     *
     * @param player The player to remove mana from
     * @param mana   The amount of mana to remove
     */
    public void removeMana(Player player, int mana) {
        String uuid = player.getUniqueId().toString();
        int currentMana = getMana(uuid);
        setMana(uuid, currentMana - mana);
        setEXPBar(player);
    }


    /**
     * Sets the EXP bar of a player to their current mana
     *
     * @param player The player to set the EXP bar of
     */
    public void setEXPBar(Player player) {
        int mana = getMana(player.getUniqueId().toString());
        player.setExp((float) mana / 100);
    }


    /**
     * Checks if a player can cast a spell
     *
     * @param player   The player to check
     * @param manaCost The mana cost of the spell
     * @return Whether the player can cast the spell
     */
    public boolean canCast(Player player, int manaCost) {
        return getMana(player.getUniqueId().toString()) >= manaCost;
    }

    /**
     * On player join, check if they have mana, if not, set it to 100
     * If they do, set their EXP bar to their current mana
     *
     * @param event The PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        boolean exists = pool.exists(event.getPlayer().getUniqueId() + ":mana");
        Player player = event.getPlayer();

        if (!exists) {
            setMana(player.getUniqueId().toString(), 100);
            setEXPBar(player);
        } else {
            setEXPBar(player);
        }
    }

    /**
     * On player death reset the expbar to their current mana
     *
     * @param event The PlayerDeathEvent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        setEXPBar(player);
    }
}
