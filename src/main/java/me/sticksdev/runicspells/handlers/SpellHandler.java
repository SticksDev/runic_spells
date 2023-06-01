package me.sticksdev.runicspells.handlers;
import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.spells.FireSpell;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import java.util.HashMap;

public class SpellHandler {
    public static HashMap<String, ItemBasedSpell> itemBasedSpells = new HashMap<>();

    public static void registerItemSpell(ItemBasedSpell spell) {
        // Check if the item provided is a valid material
        try {
            Material itemMaterial = Material.valueOf(spell.getItem());
        } catch (IllegalArgumentException e) {
            Runic_spells.getInstance().getLogger().warning("Invalid item material provided for spell " + spell.getName() + "!");
            Runic_spells.getInstance().getLogger().warning("Item material provided: " + spell.getItem());
            return;
        }

        // Check if the spell is already registered
        if (itemBasedSpells.containsKey(spell.getName())) {
            Runic_spells.getInstance().getLogger().warning("Spell " + spell.getName() + " is already registered!");
            return;
        }

        // Listen for "use" on that item
        Runic_spells.getInstance().getServer().getPluginManager().registerEvents(spell, Runic_spells.getInstance());

        // Register the spell
        itemBasedSpells.put(spell.getName(), spell);

        // Log the spell registration
        Runic_spells.getInstance().getLogger().info("Registered spell " + spell.getName() + "!");
    }

    public static ItemBasedSpell getItemSpell(String name) {
        return itemBasedSpells.get(name);
    }

    public void executeSpell(String name, Player player, Entity nearestEntity) {
        ItemBasedSpell spell = getItemSpell(name);
        if (spell != null) {
            spell.cast(player, nearestEntity);
        }
    }

    public static void registerSpells() {
        // Register spells
        Runic_spells.getInstance().getLogger().info("Registering spells...");
        registerItemSpell(new FireSpell());
    }
}
