package me.sticksdev.runicspells.structures;

public class ItemBasedSpell extends BaseSpell {
    // Spell that uses minecraft in-game item to use/cast
    public String item;
    public int amount;
    public int cooldown;
    public int manaCost;


    public ItemBasedSpell(String name, String description, String item, int amount, int cooldown, int manaCost) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.amount = amount;
        this.cooldown = cooldown;
        this.manaCost = manaCost;
    }
}
