package me.sticksdev.runicspells.spells;

import me.sticksdev.runicspells.Runic_spells;
import me.sticksdev.runicspells.structures.ItemBasedSpell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LightningSpell extends ItemBasedSpell {
    public LightningSpell() {
        super("Lightning", "Strikes lightning at the target", 4, "BLAZE_ROD", 3, 10, 0, LightningSpell::castLightning);
    }

    private static void castLightning(Player player, Entity nearestEntity) {
        if (player.getTargetBlock(null, 100).getType().isAir()) {
            // Strike lightning at the player
            player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
        } else if(nearestEntity != null) {
            // Strike lightning at the nearest entity
            player.getWorld().strikeLightning(nearestEntity.getLocation());
        } else {
            final TextComponent errorMessage = Component.text()
                    .append(Component.text("[!]", NamedTextColor.RED, TextDecoration.BOLD))
                    .append(Component.text(" You must be near an entity/mob or player to cast this spell!", NamedTextColor.RED))
                    .build();

            player.sendMessage(errorMessage);
        }
    }
}
