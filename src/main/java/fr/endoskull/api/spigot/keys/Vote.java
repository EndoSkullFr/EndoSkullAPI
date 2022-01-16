package fr.endoskull.api.spigot.keys;

import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Vote {
    ULTIME("§4Clé Ultime", 0.5, "key add %player% ultime 1", new CustomItemStack(Material.TRIPWIRE_HOOK).setGlow()),
    BOOST3("§eBOOSTER §l+100% §7(1 heure)", 10, "boost addtemp %player% 1 1h", new ItemStack(Material.RED_ROSE, 1, (byte) 2)),
    BOOST2("§eBOOSTER §l+100% §7(30 min)", 15, "boost addtemp %player% 1 30m", new ItemStack(Material.RED_ROSE, 1, (byte) 1)),
    BOOST1("§eBOOSTER §l+50% §7(1 heure)", 15, "boost addtemp %player% 0.5 1h", new ItemStack(Material.RED_ROSE, 1, (byte) 0)),
    COINS5("§e1000 Coins", 5, "coins add %player% 1000", new CustomItemStack(Material.GOLD_BLOCK).setGlow()),
    COINS4("§e500 Coins", 9.5, "coins add %player% 500", new ItemStack(Material.GOLD_BLOCK)),
    COINS3("§e150 Coins", 10, "coins add %player% 150", new CustomItemStack(Material.GOLD_INGOT).setGlow()),
    COINS2("§e100 Coins", 15, "coins add %player% 100", new ItemStack(Material.GOLD_INGOT)),
    COINS1("§e50 Coins", 20, "coins add %player% 50", new ItemStack(Material.GOLD_NUGGET));

    private String name;
    private double probability;
    private String command;
    private ItemStack item;

    Vote(String name, double probability, String command, ItemStack item) {
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
        for (Vote value : Vote.values()) {
            allProbability += value.getProbability();
        }

        return  getProbability() / (allProbability / 100);
    }

    public static Vote getByName(String name) {
        return Arrays.stream(values()).filter(r -> r.getName().equals(name)).findAny().orElse(Vote.COINS1);
    }
}
