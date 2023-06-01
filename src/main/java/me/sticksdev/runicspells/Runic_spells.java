package me.sticksdev.runicspells;

import me.sticksdev.runicspells.handlers.SpellHandler;
import me.sticksdev.runicspells.utils.Yaml;
import org.bukkit.plugin.java.JavaPlugin;

public final class Runic_spells extends JavaPlugin {
    public static Runic_spells instance;
    private final Yaml config = new Yaml(this); // TODO: Maybe make this use instance instead of this - was dealing with a null pointer exception

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("Runic Spells has been enabled!");
        getLogger().info("Loading config...");
        config.init();

        // Register spells
        SpellHandler.registerSpells();
    }

    @Override
    public void onDisable() {
        config.destroy();
    }

    public static Runic_spells getInstance() {
        return instance;
    }
}
