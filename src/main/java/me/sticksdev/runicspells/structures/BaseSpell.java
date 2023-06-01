package me.sticksdev.runicspells.structures;

/**
 * The base class for spells, only contains a name and a description
 */
public class BaseSpell {
    public String name;
    public String description;
    public int spellID;

    public BaseSpell(String name, String description, int spellID) {
        this.name = name;
        this.description = description;
        this.spellID = spellID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSpellID() {
        return spellID;
    }
}
