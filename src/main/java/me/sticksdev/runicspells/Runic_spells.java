package me.sticksdev.runicspells;

import me.sticksdev.runicspells.commands.ReloadCmd;
import me.sticksdev.runicspells.handlers.CooldownHandler;
import me.sticksdev.runicspells.handlers.ManaHandler;
import me.sticksdev.runicspells.handlers.SpellHandler;
import me.sticksdev.runicspells.utils.Redis;
import me.sticksdev.runicspells.utils.Yaml;
import org.bukkit.plugin.java.JavaPlugin;

public final class Runic_spells extends JavaPlugin {
    public static Runic_spells instance;
    private static Redis redis;
    private static Yaml config;
    private static CooldownHandler cooldownHandler;
    private static ManaHandler manaHandler;
    private static SpellHandler spellHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("Runic Spells has been enabled!");
        getLogger().info("Loading config...");

        // Load config
        config = new Yaml();
        config.init();

        // Init Redis
        redis = new Redis();
        redis.init();

        // Init cooldown handler
        cooldownHandler = new CooldownHandler();

        // Register mana handler
        manaHandler = new ManaHandler();
        manaHandler.startTimers();
        getServer().getPluginManager().registerEvents(manaHandler, this);

        // Register spells
        spellHandler = new SpellHandler();
        SpellHandler.registerSpells();

        // Register commands
        getCommand("reloadrs").setExecutor(new ReloadCmd());
    }

    @Override
    public void onDisable() {
        config.destroy();
        redis.close();
        manaHandler.destroyTimers();
    }

    public static Runic_spells getInstance() {
        return instance;
    }

    public Yaml getConfigHandler() {
        return config;
    }

    public Redis getRedisHandler() {
        return redis;
    }

    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }

    public ManaHandler getManaHandler() {
        return manaHandler;
    }

    public SpellHandler getSpellHandler() {
        return spellHandler;
    }
}
