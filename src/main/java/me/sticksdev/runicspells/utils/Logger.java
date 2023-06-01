package me.sticksdev.runicspells.utils;

import me.sticksdev.runicspells.Runic_spells;
import org.jetbrains.annotations.NotNull;

public class Logger {
    private static final Runic_spells plugin = Runic_spells.getInstance();
    private static final java.util.logging.@NotNull Logger logger = plugin.getLogger();

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warning(message);
    }

    public static void severe(String message) {
        logger.severe(message);
    }
}
