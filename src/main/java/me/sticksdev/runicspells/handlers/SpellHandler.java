package me.sticksdev.runicspells.handlers;
import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.spells.EarthSpell;
import me.sticksdev.runicspells.spells.FireSpell;
import me.sticksdev.runicspells.spells.LightningSpell;
import me.sticksdev.runicspells.spells.WaterSpell;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import me.sticksdev.runicspells.structures.SpellOverride;
import me.sticksdev.runicspells.utils.Logger;
import me.sticksdev.runicspells.utils.Yaml;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.Material;

import java.util.HashMap;

public class SpellHandler {
    public static HashMap<String, ItemBasedSpell> itemBasedSpells = new HashMap<>();
    private static final Yaml config = Runic_spells.getInstance().getConfigHandler();
    private static final Runic_spells plugin = Runic_spells.getInstance();


    public static void registerItemSpell(ItemBasedSpell spell) {
        // Check if the item provided is a valid material
        try {
            Material itemMaterial = Material.valueOf(spell.getItem());
        } catch (IllegalArgumentException e) {
            Logger.warning("Spell " + spell.getName() + " has an invalid item material!");
            Logger.warning("Provided material: " + spell.getItem());
            return;
        }

        // Check if the spell is already registered
        if (itemBasedSpells.containsKey(spell.getName())) {
            Logger.warning("Spell " + spell.getName() + " is already registered!");
            return;
        }

        // Check if the spell is enabled
        boolean spellEnabled = config.getIsSpellEnabled(spell.getName());
        if (!spellEnabled) {
            Logger.warning("Spell " + spell.getName() + " is disabled in the config - not registering!");
            return;
        }

        // Check if any overrides are present
        SpellOverride override = config.getSpellOverrides(spell.getName());

        if(override != null) {
            spell.setOverrides(override);
            Logger.info("Spell " + spell.getName() + " overrides set!");
        }

        // Listen for "use" on that item
        plugin.getServer().getPluginManager().registerEvents(spell, plugin);

        // Register the spell
        itemBasedSpells.put(spell.getName(), spell);

        // Log the spell registration
        Logger.info("Registered spell " + spell.getName() + "!");
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
        Logger.info("Registering spells...");
        registerItemSpell(new FireSpell());
        registerItemSpell(new WaterSpell());
        registerItemSpell(new EarthSpell());
        registerItemSpell(new LightningSpell());
        Logger.info("Registered " + itemBasedSpells.size() + " spells!");
    }
}
