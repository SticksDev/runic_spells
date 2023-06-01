package me.sticksdev.runicspells.utils;

import me.sticksdev.runicspells.Runic_spells;
import redis.clients.jedis.CommandObject;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;

public class Redis {
    private final Runic_spells plugin = Runic_spells.getInstance();
    private final String host = plugin.getConfigHandler().getConfig().getString("redis.host");
    private final int port = plugin.getConfigHandler().getConfig().getInt("redis.port");

    // Create a new JedisPooled instance
    private static JedisPooled pool;

    public void init() {
        try {
            Logger.info("Connecting to Redis...");
            pool = new JedisPooled(host, port);
            Logger.info("JedisPooled instance created, testing connection...");
            pool.set("test", "test");
            pool.del("test");
            Logger.info("Successfully connected to Redis!");
        } catch (Exception e) {
            Logger.severe("Failed to connect to Redis - see stack trace below:");
            e.printStackTrace();
            Logger.severe("Disabling plugin due to Redis connection failure...");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public JedisPooled getPool() {
        return pool;
    }

    public void close() {
        pool.close();
    }
}
