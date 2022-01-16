package fr.endoskull.api.spigot.keys;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Ultime {
    GENERAL("§cGrade Général §7(1 mois)", 2, "lp user %player% parent addtemp general 1mo", new ItemStack(Material.DIAMOND_SWORD)),
    OFFICIER("§eGrade Officier §7(1 mois)", 6, "lp user %player% parent addtemp officier 1mo", new ItemStack(Material.GOLD_SWORD)),
    DOUBLE("§42 Clés Ultimes", 2, "key add %player% ULTIME 2", new ItemStack(Material.TRIPWIRE_HOOK, 2)),
    BOOSTER0("§eBOOSTER §l+50% §7(30 jours)", 30, "boost addtemp %player% 0.5 30d", new ItemStack(Material.RED_ROSE, 1, (byte) 8)),
    BOOSTER1("§eBOOSTER §l+100% §7(30 jours)", 25, "boost addtemp %player% 1 30d", new ItemStack(Material.RED_ROSE, 1, (byte) 7)),
    BOOSTER2("§eBOOSTER §l+100% §7(14 jours)", 25, "boost addtemp %player% 1 14d", new ItemStack(Material.RED_ROSE, 1, (byte) 6)),
    BOOSTER3("§eBOOSTER §l+150% §7(14 jours)", 20, "boost addtemp %player% 1.5 14d", new ItemStack(Material.RED_ROSE, 1, (byte) 5)),
    BOOSTER4("§eBOOSTER §l+150% §7(7 jours)", 20, "boost addtemp %player% 1.5 7d", new ItemStack(Material.RED_ROSE, 1, (byte) 4)),
    BOOSTER5("§eBOOSTER §l+200% §7(7 jours)", 15, "boost addtemp %player% 2 7d", new ItemStack(Material.RED_ROSE, 1, (byte) 3)),
    BOOSTER6("§eBOOSTER §l+200% §7(3 jours)", 15, "boost addtemp %player% 2 3d", new ItemStack(Material.RED_ROSE, 1, (byte) 2)),
    BOOSTER7("§eBOOSTER §l+250% §7(3 jours)", 15, "boost addtemp %player% 2.5 3d", new ItemStack(Material.RED_ROSE, 1, (byte) 1)),
    BOOSTER8("§eBOOSTER §l+250% §7(1 jours)", 15, "boost addtemp %player% 2.5 1d", new ItemStack(Material.RED_ROSE, 1, (byte) 0)),
    BOOSTER9("§eBOOSTER §l+300% §7(1 jours)", 10, "boost addtemp %player% 3 1d", new ItemStack(Material.YELLOW_FLOWER));

    //50-100 30j 100-150 14j 150-200 7j 200-250 3j 250-300 1j

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
