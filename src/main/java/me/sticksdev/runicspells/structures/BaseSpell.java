package me.sticksdev.runicspells.structures;

/**
 * The base class for spells, only contains a name and a description
 */
public class BaseSpell {
    public String name;
    public String description;
    public int spellID;
    public int range;

    /**
     * Creates a new BaseSpell object. All parameters are required.
     *
     * @param name        The name of the spell.
     * @param description The description of the spell.
     * @param range       The range of the spell.
     * @param spellID     The ID of the spell, hardcoded in the spell's class and not editable.
     */
    public BaseSpell(String name, String description, int range, int spellID) {
        this.name = name;
        this.description = description;
        this.spellID = spellID;
        this.range = range;
    }

    /**
     * Gets the name of the spell.
     *
     * @return The name of the spell.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the spell.
     *
     * @return The description of the spell.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the range of the spell.
     *
     * @return The range of the spell.
     */
    public int getSpellID() {
        return spellID;
    }
}
