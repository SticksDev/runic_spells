package me.sticksdev.runicspells.structures;

/**
 * The base class for spells, only contains a name and a description
 */
public class BaseSpell {
    public String name;
    public String description;

    public BaseSpell(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
