package me.sticksdev.runicspells.utils;

import me.sticksdev.runicspells.Runic_spells;
import org.jetbrains.annotations.NotNull;

/**
 * A simple logger class for the plugin.
 * Uses the plugin's logger to log messages.
 * See {@link java.util.logging.Logger} for more information.
 */
public class Logger {
    private static final Runic_spells plugin = Runic_spells.getInstance();
    private static final java.util.logging.@NotNull Logger logger = plugin.getLogger();

    /**
     * Logs a message to the console.
     *
     * @param message The message to log.
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs a warning to the console.
     *
     * @param message The message to log.
     */
    public static void warning(String message) {
        logger.warning(message);
    }

    /**
     * Logs a severe error to the console.
     *
     * @param message The message to log.
     */
    public static void severe(String message) {
        logger.severe(message);
    }
}
