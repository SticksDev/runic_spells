package me.sticksdev.runicspells.utils;
import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.SpellOverride;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Yaml {
    // Basic YAML Loader and data holder for minecraft plugins
    private final Runic_spells plugin = Runic_spells.getInstance();
    public FileConfiguration spellsConfig;
    public FileConfiguration configConfig;
    private File spellsFilePath;
    private File configFilePath;


    /**
     * Loads the config file from the plugin's data folder or creates it if it doesn't exist
     */
    public void init() {
        spellsFilePath = new File(plugin.getDataFolder(), "spells.yml");
        configFilePath = new File(plugin.getDataFolder(), "config.yml");

        if (!spellsFilePath.exists()) {
            Logger.info("Spells.yml not found, creating...");
            spellsFilePath.getParentFile().mkdirs();
            plugin.saveResource("spells.yml", false);
        }

        if (!configFilePath.exists()) {
            Logger.info("Config.yml not found, creating...");
            configFilePath.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        spellsConfig = new YamlConfiguration();
        try {
            spellsConfig.load(spellsFilePath);
            Logger.info("Spells.yml loaded!");
        } catch (IOException | InvalidConfigurationException e) {
            Logger.severe("Spells.yml failed to load - see stack trace below:");
            e.printStackTrace();
        }

        // Load config.yml
        configConfig = new YamlConfiguration();
        try {
            configConfig.load(configFilePath);
            Logger.info("Config.yml loaded!");
        }  catch (IOException | InvalidConfigurationException e) {
            Logger.severe("Config.yml failed to load - see stack trace below:");
            e.printStackTrace();
        }
    }

    /**
     * Cleans up when plugin is disabled
     */
    public void destroy() {
        spellsConfig = null;
        spellsFilePath = null;
        configConfig = null;
        configFilePath = null;
    }


    /**
     * Returns the currently loaded spells config file
     * @return FileConfiguration
     * @throws IllegalStateException if the config file hasn't been loaded yet
     */
    public FileConfiguration getSpellsConfig() {
        if (spellsConfig == null) {
            throw new IllegalStateException("YamlError: Config not loaded yet, have you called init()?");
        }

        return spellsConfig;
    }

    /**
     * Returns the currently loaded config file
     * @return FileConfiguration
     * @throws IllegalStateException if the config file hasn't been loaded yet
     */
    public FileConfiguration getConfig() {
        if (configConfig == null) {
            throw new IllegalStateException("YamlError: Config not loaded yet, have you called init()?");
        }

        return configConfig;
    }

    @Nullable
    public SpellOverride getSpellOverrides(String spellName) {
        ConfigurationSection spellOverridesBlock = getSpellsConfig().getConfigurationSection("overrideSpells");

        if (spellOverridesBlock == null) {
            Logger.warning("overrideSpells is either null or commented out in spells.yml - not returning any overrides");
            return null;
        }

        ConfigurationSection spellOverrides = spellOverridesBlock.getConfigurationSection(spellName);

        if (spellOverrides == null) {
            Logger.warning("Spell overrides for " + spellName + " is either null or commented out in spells.yml - not returning any overrides");
            return null;
        }

        String overrideSpellTool = spellOverrides.getString("OverrideTool");
        String overrideManaCost = spellOverrides.getString("OverrideManaCost");
        String overrideCooldown = spellOverrides.getString("OverrideCooldown");
        String overrideDamage = spellOverrides.getString("OverrideDamage");

        return new SpellOverride(overrideSpellTool, overrideManaCost, overrideCooldown, overrideDamage);
    }


    public boolean getIsSpellEnabled(String spellName) {
        // Check if it's in the enabledSpells list
        List<?> enabledSpells = getSpellsConfig().getList("enabledSpells");

        if (enabledSpells == null) {
            Logger.warning("enabledSpells is either null or commented out in spells.yml - not returning any overrides");
            return false;
        }

        return enabledSpells.contains(spellName);
    }
}