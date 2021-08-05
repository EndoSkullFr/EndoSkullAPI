package fr.endoskull.api.keys;

import fr.endoskull.api.utils.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Vote {
    ULTIME("§4Clé Ultime", 0.5, "key add %player% ultime 1", new CustomItemStack(Material.TRIPWIRE_HOOK).setGlow()),
    KITS("§9Clé Kits", 5, "key add %player% kits 1", new ItemStack(Material.TRIPWIRE_HOOK)),
    COINS("§eClé Coins", 10, "key add %player% coins 1", new ItemStack(Material.TRIPWIRE_HOOK)),
    DOUBLE("§e2 Clés Votes", 2.5, "key add %player% VOTE 2", new ItemStack(Material.TRIPWIRE_HOOK, 2)),
    COINS3("§e150 Coins", 12, "coins add %player% 150", new ItemStack(Material.GOLD_NUGGET, 3)),
    COINS2("§e100 Coins", 30, "coins add %player% 100", new ItemStack(Material.GOLD_NUGGET, 2)),
    COINS1("§e50 Coins", 40, "coins add %player% 50", new ItemStack(Material.GOLD_NUGGET, 1));

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
