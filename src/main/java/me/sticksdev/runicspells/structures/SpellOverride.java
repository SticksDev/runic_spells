package me.sticksdev.runicspells.structures;
import org.jetbrains.annotations.Nullable;

public class SpellOverride {
    @Nullable
    String OverrideTool;

    @Nullable
    Integer OverrideManaCost;

    @Nullable
    Integer OverrideCooldown;

    @Nullable
    Integer OverrideDamage;

    public SpellOverride(@Nullable String overrideSpellTool, @Nullable String overrideManaCost, @Nullable String overrideCooldown, @Nullable String overrideDamage) {
        if (overrideSpellTool != null) {
            this.OverrideTool = overrideSpellTool;
        }

        if (overrideManaCost != null) {
            this.OverrideManaCost = Integer.parseInt(overrideManaCost);
        }

        if (overrideCooldown != null) {
            this.OverrideCooldown = Integer.parseInt(overrideCooldown);
        }

        if (overrideDamage != null) {
            this.OverrideDamage = Integer.parseInt(overrideDamage);
        }
    }
}
