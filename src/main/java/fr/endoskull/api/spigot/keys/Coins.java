package fr.endoskull.api.spigot.keys;

import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Coins {
    DOUBLE("§e2 Clés Coins", 5, "key add %player% coins 1", new CustomItemStack(Material.TRIPWIRE_HOOK).setGlow()),
    ULTIMECOINS("§e1000 coins", 5, "coins add %player% 1000", new ItemStack(Material.GOLD_BLOCK, 5)),
    MAXCOINS("§e500 coins", 10, "coins add %player% 500", new ItemStack(Material.GOLD_BLOCK, 1)),
    FIRSTCOINS("§e250 coins", 15, "coins add %player% 250", new ItemStack(Material.GOLD_INGOT, 5)),
    SECONDCOINS("§e200 coins", 15, "coins add %player% 200", new ItemStack(Material.GOLD_INGOT, 1)),
    THIRDCOINS("§e150 coins", 15, "coins add %player% 150", new ItemStack(Material.GOLD_NUGGET, 5)),
    FOORTHCOINS("§e100 coins", 10, "coins add %player% 100", new ItemStack(Material.GOLD_NUGGET, 1)),
    ANYTHINK("§eRien", 25, "", new ItemStack(Material.BARRIER));

    private String name;
    private double probability;
    private String command;
    private ItemStack item;

    Coins(String name, double probability, String command, ItemStack item) {
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
        for (Coins value : Coins.values()) {
            allProbability += value.getProbability();
        }

        return  getProbability() / (allProbability / 100);
    }

    public static Coins getByName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(Coins.ANYTHINK);
    }
}
