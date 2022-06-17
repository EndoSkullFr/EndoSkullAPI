package fr.endoskull.api.spigot.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum SanctionEnum {
    CHEAT(9, "§cCheat", true, "#Cheat", new ItemStack(Material.IRON_SWORD)),
    CROSSTEAM(11, "§cCrossTeam", true, "#CrossTeam", new ItemStack(Material.IRON_SWORD)),
    PUB(13, "§cPub", true, "#Pub", new ItemStack(Material.IRON_SWORD)),
    INSULTES(15, "§eInsultes", false, "#Insultes", new ItemStack(Material.IRON_SWORD)),
    DISCRIMINATION(17, "§eDiscriminations (Racisme/Homophobie)", false, "#Discriminations", new ItemStack(Material.IRON_SWORD));

    private int slot;
    private String name;
    private boolean ban;
    private String template;
    private ItemStack item;

    SanctionEnum(int slot, String name, boolean ban, String template, ItemStack item) {
        this.slot = slot;
        this.name = name;
        this.ban = ban;
        this.template = template;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public boolean isBan() {
        return ban;
    }

    public String getTemplate() {
        return template;
    }

    public ItemStack getItem() {
        return item;
    }
}
