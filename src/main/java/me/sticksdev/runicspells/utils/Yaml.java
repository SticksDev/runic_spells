package me.sticksdev.runicspells.utils;
import me.sticksdev.runicspells.Runic_spells;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Yaml {
    // Basic YAML Loader and data holder for minecraft plugins
    private final Runic_spells plugin;
    public FileConfiguration spellsConfig;
    private File spellsFilePath;

    public Yaml(Runic_spells plugin) {
        this.plugin = plugin;
    }


    /**
     * Loads the config file from the plugin's data folder or creates it if it doesn't exist
     */
    public void init() {
        spellsFilePath = new File(plugin.getDataFolder(), "spells.yml");

        if (!spellsFilePath.exists()) {
            plugin.getLogger().info("Spells.yml not found, creating...");
            spellsFilePath.getParentFile().mkdirs();
            plugin.saveResource("spells.yml", false);
        }

        spellsConfig = new YamlConfiguration();
        try {
            spellsConfig.load(spellsFilePath);
            plugin.getLogger().info("Spells.yml loaded!");
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().warning("Spells.yml failed to load - see stack trace below:");
            e.printStackTrace();
        }
    }

    /**
     * Cleans up when plugin is disabled
     */
    public void destroy() {
        spellsConfig = null;
        spellsFilePath = null;
    }


    /**
     * Returns the currently loaded config file
     * @return FileConfiguration
     * @throws IllegalStateException if the config file hasn't been loaded yet
     */
    public FileConfiguration getConfig() {
        if (spellsConfig == null) {
            throw new IllegalStateException("YamlError: Config not loaded yet, have you called init()?");
        }

        return spellsConfig;
    }
}
