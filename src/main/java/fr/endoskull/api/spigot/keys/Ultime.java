package fr.endoskull.api.spigot.keys;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Ultime {
    GENERAL("§cGrade Général", 1, "lp user %player% parent add general", new ItemStack(Material.DIAMOND_SWORD)),
    OFFICIER("§eGrade Officier", 3, "lp user %player% parent add officier", new ItemStack(Material.GOLD_SWORD)),
    DOUBLE("§42 Clés Ultimes", 1, "key add %player% ULTIME 2", new ItemStack(Material.TRIPWIRE_HOOK, 2)),
    BOOSTER4("§eBooster §lx4", 5, "boost add %player% 3", new ItemStack(Material.RED_ROSE, 1, (byte) 4)),
    BOOSTER3("§eBooster §lx3", 20, "boost add %player% 2", new ItemStack(Material.RED_ROSE, 1, (byte) 5)),
    BOOSTER2("§eBooster §lx2", 30, "boost add %player% 1", new ItemStack(Material.RED_ROSE, 1, (byte) 7)),
    BOOSTER1("§eBooster §lx1.5", 40, "boost add %player% 0.5", new ItemStack(Material.RED_ROSE, 1, (byte) 6));

    private String name;
    private double probability;
    private String command;
    private ItemStack item;

    Ultime(String name, double probability, String command, ItemStack item) {
        this.name = name;
        this.probability = probability;
        this.command = command;
        this.item = item;
    }

    public String getName() {
        return name;
    }
    public double getProbability() {
        return probability;
    }
    public String getCommand() {
        return command;
    }
    public ItemStack getItem() {
        return item;
    }

    public double getPourcent() {
        double allProbability = 0;
        for (Ultime value : Ultime.values()) {
            allProbability += value.getProbability();
        }

        return  getProbability() / (allProbability / 100);
    }

    public static Ultime getByName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(Ultime.BOOSTER1);
    }
}
