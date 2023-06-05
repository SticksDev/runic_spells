package me.sticksdev.runicspells.structures;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.handlers.CooldownHandler;
import me.sticksdev.runicspells.handlers.ManaHandler;
import me.sticksdev.runicspells.utils.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * The main spells class that most spells extend from
 * This class is used for spells that use an item to cast (e.g. a stick)
 */
public class ItemBasedSpell extends BaseSpell implements Listener {
    // Spell that uses Minecraft in-game item to use/cast
    private String item;
    private int cooldown;
    private int manaCost;
    private int damage;
    private boolean requiresNearbyEntity;
    private BiConsumer<Player, Entity> castHandler;
    private final CooldownHandler cooldownHandler = Runic_spells.getInstance().getCooldownHandler();
    private final ManaHandler manaHandler = Runic_spells.getInstance().getManaHandler();

    /**
     * Creates a new ItemBasedSpell object. All parameters are required (some are pulled from BaseSpell).
     *
     * @param name                 The name of the spell
     * @param description          The description of the spell
     * @param spellId              The ID of the spell
     * @param range                The range of the spell (how far away it can be cast, in blocks)
     * @param item                 The item to cast the spell with (e.g. "stick")
     * @param cooldown             The cooldown of the spell (in seconds)
     * @param manaCost             The mana cost of the spell
     * @param damage               The damage to the spell (NOTE: some spells may not use this due to API limitations)
     * @param requiresNearbyEntity Whether the spell requires a nearby entity to cast
     * @param castHandler          The handler to run when the spell is cast
     */
    public ItemBasedSpell(String name, String description, int spellId, int range, String item, int cooldown, int manaCost, int damage, boolean requiresNearbyEntity, BiConsumer<Player, Entity> castHandler) {
        super(name, description, range, spellId);
        this.name = name;
        this.description = description;
        this.item = item;
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.castHandler = castHandler;
        this.damage = damage;
        this.requiresNearbyEntity = requiresNearbyEntity;
        this.range = range;
    }

    /**
     * Gets the item to cast the spell with
     *
     * @return The item to cast the spell with
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the item to cast the spell with
     *
     * @param item The item to cast the spell with
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Gets the cooldown of the spell
     *
     * @return The cooldown of the spell
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Sets the cooldown of the spell
     *
     * @param cooldown The cooldown of the spell
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Gets the mana cost of the spell
     *
     * @return The mana cost of the spell
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Sets the mana cost of the spell
     *
     * @param manaCost The mana cost of the spell
     */
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    /**
     * Calls the BiConsumer cast handler of the spell and then casts the spell
     */
    public void cast(Player player, Entity nearestEntity) {
        if (castHandler != null) {
            castHandler.accept(player, nearestEntity);
        }
    }

    /**
     * Gets the damage of the spell
     *
     * @return The damage of the spell
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage of the spell
     *
     * @param damage The damage of the spell
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Sets the spell's range (how far away it can be cast, in blocks)
     *
     * @param range The spell's range (how far away it can be cast, in blocks)
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * The main handler for spells that use an item to cast
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // TODO: Little messy, could clean this up later
        if (event.getItem() != null && event.getItem().getType() != Material.AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR)) {
            if (event.getItem() != null && event.getItem().getType().toString().equals(item)) {
                // Check if they have an active cooldown
                Player player = event.getPlayer();
                double cooldown = cooldownHandler.getCooldown(player, this);

                if (cooldown == 0) {
                    // No cooldown check if they have enough mana
                    boolean canCast = manaHandler.canCast(player, this.manaCost);
                    if (canCast) {
                        // Get nearest entity (in player's line of sight)
                        Entity nearestEntity = player.getTargetEntity(this.range);
                        if (nearestEntity == null && requiresNearbyEntity) {
                            // No entity found, so tell them
                            final TextComponent manaMessage = Component.text()
                                    .append(Component.text("[!]", NamedTextColor.RED, TextDecoration.BOLD))
                                    .append(Component.text(" Could not find a nearby entity or player to cast "))
                                    .append(Component.text(this.name, NamedTextColor.BLUE, TextDecoration.BOLD))
                                    .append(Component.text("!"))
                                    .build();

                            player.sendMessage(manaMessage);
                            return;
                        }

                        cast(player, nearestEntity);
                        cooldownHandler.setCooldown(player, this, this.cooldown);
                        manaHandler.removeMana(player, this.manaCost);

                        final TextComponent manaMessage = Component.text()
                                .append(Component.text("[RS]", NamedTextColor.GREEN, TextDecoration.BOLD))
                                .append(Component.text(" You cast "))
                                .append(Component.text(this.name, NamedTextColor.BLUE, TextDecoration.BOLD))
                                .append(Component.text(" for "))
                                .append(Component.text(this.manaCost, NamedTextColor.BLUE, TextDecoration.BOLD))
                                .append(Component.text(" mana."))
                                .build();

                        player.sendMessage(manaMessage);
                    } else {
                        // They don't have enough mana, so tell them
                        final TextComponent manaMessage = Component.text()
                                .append(Component.text("[!]", NamedTextColor.RED, TextDecoration.BOLD))
                                .append(Component.text(" You do not have enough mana to cast "))
                                .append(Component.text(this.name, NamedTextColor.BLUE, TextDecoration.BOLD))
                                .append(Component.text("!"))
                                .build();

                        player.sendMessage(manaMessage);
                    }
                } else {
                    // Cooldown is active, so tell them how long they have to wait
                    // this is horrible, but paper has forced my hand
                    final TextComponent cooldownMessage = Component.text()
                            .append(Component.text("[!]", NamedTextColor.RED, TextDecoration.BOLD))
                            .append(Component.text(" You must wait "))
                            .append(Component.text(String.format("%.1fs", cooldown), NamedTextColor.RED, TextDecoration.BOLD))
                            .append(Component.text(" before casting "))
                            .append(Component.text(this.name, NamedTextColor.BLUE, TextDecoration.BOLD))
                            .append(Component.text(" again."))
                            .build();

                    player.sendMessage(cooldownMessage);
                }
            }
        }
    }

    /**
     * Set's the spell's overrides
     *
     * @param overrides The spell's overrides (nullable)
     */
    public void setOverrides(@Nullable SpellOverride overrides) {
        if (overrides != null) {
            if (overrides.OverrideTool != null) {
                // Validate tool
                try {
                    Material.valueOf(overrides.OverrideTool);
                    this.item = overrides.OverrideTool;
                    Logger.info("Overriding item material for spell " + this.name + " to " + overrides.OverrideTool);
                } catch (IllegalArgumentException e) {
                    Logger.warning("Invalid item material provided for spell " + this.name + "!");
                    Logger.warning("Item material provided: " + overrides.OverrideTool);
                    Logger.warning("Falling back to default item material.");
                }
            }
            if (overrides.OverrideManaCost != null && overrides.OverrideManaCost != 0) {
                this.manaCost = overrides.OverrideManaCost;
                Logger.info("Overriding mana cost for spell " + this.name + " to " + overrides.OverrideManaCost);
            }
            if (overrides.OverrideCooldown != null && overrides.OverrideCooldown != 0) {
                this.cooldown = overrides.OverrideCooldown;
                Logger.info("Overriding cooldown for spell " + this.name + " to " + overrides.OverrideCooldown);
            }
            if (overrides.OverrideDamage != null && overrides.OverrideDamage != 0) {
                this.damage = overrides.OverrideDamage;
                Logger.info("Overriding damage for spell " + this.name + " to " + overrides.OverrideDamage);
            }
            if (overrides.OverrideRange != null && overrides.OverrideRange != 0) {
                this.range = overrides.OverrideRange;
                Logger.info("Overriding range for spell " + this.name + " to " + overrides.OverrideRange);
            }
        } else {
            Logger.warning("setOverrides() was called for spell " + this.name + " but no overrides were provided - falling back to defaults.");
        }
    }
}
