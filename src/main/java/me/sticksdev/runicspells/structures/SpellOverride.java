package me.sticksdev.runicspells.structures;

import org.jetbrains.annotations.Nullable;


/**
 * Class to handle overriding a spell's values. (e.g. overriding a spell's tool, mana cost, cooldown, damage, or range)
 */
public class SpellOverride {
    @Nullable
    String OverrideTool;

    @Nullable
    Integer OverrideManaCost;

    @Nullable
    Integer OverrideCooldown;

    @Nullable
    Integer OverrideDamage;

    @Nullable
    Integer OverrideRange;

    /**
     * Creates a new SpellOverride object. If any of the parameters are null, they will not be overridden and default to the spell's values.
     *
     * @param overrideSpellTool The tool to override the spell's tool with.
     * @param overrideManaCost  The mana cost to override the spell's mana cost with.
     * @param overrideCooldown  The cooldown to override the spell's cooldown with.
     * @param overrideDamage    The damage to override the spell's damage with.
     * @param overrideRange     The range to override the spell's range with.
     */
    public SpellOverride(@Nullable String overrideSpellTool, int overrideManaCost, int overrideCooldown, int overrideDamage, int overrideRange) {
        if (overrideSpellTool != null) {
            this.OverrideTool = overrideSpellTool;
        }

        if (overrideManaCost != 0) {
            this.OverrideManaCost = overrideManaCost;
        }

        if (overrideCooldown != 0) {
            this.OverrideCooldown = overrideCooldown;
        }

        if (overrideDamage != 0) {
            this.OverrideDamage = overrideDamage;
        }

        if (overrideRange != 0) {
            this.OverrideRange = overrideRange;
        }
    }
}
