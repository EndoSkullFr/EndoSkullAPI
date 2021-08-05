package fr.endoskull.api.keys;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Ultime {
    DIAMANT("§bGrade Diamant", 0.5, "rank add %player% diamant", new ItemStack(Material.DIAMOND)),
    OR("§eGrade Or", 1, "rank add %player% or", new ItemStack(Material.GOLD_INGOT)),
    FER("§fGrade Fer", 2.5, "rank add %player% fer", new ItemStack(Material.IRON_INGOT)),
    DOUBLE("§42 Clés Ultimes", 1, "key add %player% ULTIME 2", new ItemStack(Material.TRIPWIRE_HOOK, 2)),
    BOOSTER4("§eBooster §lx4", 5, "boost %player% add 3", new ItemStack(Material.RED_ROSE, 1, (byte) 4)),
    BOOSTER3("§eBooster §lx3", 20, "boost %player% add 2", new ItemStack(Material.RED_ROSE, 1, (byte) 5)),
    BOOSTER2("§eBooster §lx2", 30, "boost %player% add 1", new ItemStack(Material.RED_ROSE, 1, (byte) 7)),
    BOOSTER1("§eBooster §lx1.5", 40, "boost %player% add 0.5", new ItemStack(Material.RED_ROSE, 1, (byte) 6));

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
