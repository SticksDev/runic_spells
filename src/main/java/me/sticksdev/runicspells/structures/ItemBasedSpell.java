package me.sticksdev.runicspells.structures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.BiConsumer;

public class ItemBasedSpell extends BaseSpell implements Listener {
    // Spell that uses Minecraft in-game item to use/cast
    private String item;
    private int cooldown;
    private int manaCost;
    private int damage;
    private final BiConsumer<Player, Entity> castHandler;

    public ItemBasedSpell(String name, String description, String item, int cooldown, int manaCost, int damage, BiConsumer<Player, Entity> castHandler) {
        super(name, description);
        this.name = name;
        this.description = description;
        this.item = item;
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.castHandler = castHandler;
        this.damage = damage;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public void cast(Player player, Entity nearestEntity) {
        if (castHandler != null) {
            castHandler.accept(player, nearestEntity);
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    // Listen to the interact event for only the item specified
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType().toString().equals(item)) {
            // For now, just cast the spell on the nearest entity
            cast(event.getPlayer(), event.getPlayer().getNearbyEntities(5, 5, 5).stream().findFirst().orElse(null));
        }
    }
}
