package me.sticksdev.runicspells.utils;
import me.sticksdev.runicspells.Runic_spells;
import redis.clients.jedis.JedisPooled;


/**
 * Redis connection handler
 * This class is used to handle the Redis connections and
 * operations for the plugin
 */
public class Redis {
    private final Runic_spells plugin = Runic_spells.getInstance();
    private final String host = plugin.getConfigHandler().getConfig().getString("redis.host");
    private final int port = plugin.getConfigHandler().getConfig().getInt("redis.port");

    // Create a new JedisPooled instance
    private static JedisPooled pool;

    /**
     * Initialize the Redis connection for the current instance
     */
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

    /**
     * Gets the current pool handle of the instance
     *
     * @return JedisPooled instance
     */
    public JedisPooled getPool() {
        return pool;
    }

    /**
     * Shuts down the current Redis connection
     * This is only called on plugin disable
     */
    public void close() {
        pool.close();
    }
}
