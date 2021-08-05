package fr.endoskull.api.keys;

import fr.endoskull.api.Main;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoxInventory {
    /*private static HashMap<Integer, Integer> tickStep = new HashMap<Integer, Integer>() {{
        put(1, 40);
        put(2, 20);
        put(4, 5);
        put(8, 3);
        put(15, 2);
        put(20, 1);
        put(1, 20);
        put(2, 10);
        put(5, 5);
        put(10, 2);
        put(20, 1);
    }
    };*/
    private static List<Integer> ticks = new ArrayList<Integer>() {{
        for (int i = 0; i < 20; i++) {
            add(1);
        }
        for (int i = 0; i < 10; i++) {
            add(2);
        }
        for (int i = 0; i < 5; i++) {
            add(5);
        }
        for (int i = 0; i < 2; i++) {
            add(10);
        }
        for (int i = 0; i < 1; i++) {
            add(20);
        }
    }};
    public static void openUltime(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§4Box Ultime");
        int i = 0;
        for (Ultime value : Ultime.values()) {
            inv.setItem(i, new CustomItemStack(value.getItem()).setName(value.getName()).setLore("\n§7Probabilité: " + value.getName().substring(0, 2) + value.getPourcent() + "%"));
            i++;
        }
        inv.setItem(22, new CustomItemStack(Material.ANVIL).setName("§6Ouvrir").setLore("\n§7Vous avez §6" + Keys.getKeys(Main.getInstance().getUuidsByName().get(player.getName()), "ultime") + " §7Clé(s) Ultime(s)"));
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 50, 50);
    }


    public static void openVote(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§eBox Vote");
        int i = 0;
        for (Vote value : Vote.values()) {
            inv.setItem(i, new CustomItemStack(value.getItem()).setName(value.getName()).setLore("\n§7Probabilité: " + value.getName().substring(0, 2) + value.getPourcent() + "%"));
            i++;
        }
        inv.setItem(22, new CustomItemStack(Material.ANVIL).setName("§6Ouvrir").setLore("\n§7Vous avez §6" + Keys.getKeys(Main.getInstance().getUuidsByName().get(player.getName()), "vote") + " §7Clé(s) Vote(s)"));
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 50, 50);
    }

    public static void playUltimeAnimation(Player player) {
        List<ItemStack> items = new ArrayList<>();
        for (Ultime value : Ultime.values()) {
            for (int i = 0; i < value.getProbability() * 2; i++) {
                items.add(new CustomItemStack(value.getItem()).setName(value.getName()));
            }
        }
        Collections.shuffle(items);
        Inventory inv = Bukkit.createInventory(null, 27, "§4Clé Ultime");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, CustomItemStack.getPane(14).setName("§7").setGlow(i == 4 || i == 22));
        }
        int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
        int avancement = 0;
        for (int i = 0; i < 9; i++) {
            inv.setItem(slots[i], items.get(avancement + i));
        }

        player.openInventory(inv);
        if (!Main.getInstance().getOpeningKeys().containsKey(player)) Main.getInstance().getOpeningKeys().put(player, inv);
        scheduleUltime(inv, items, player, 0);

    }
    public static void scheduleUltime(Inventory inv, final List<ItemStack> items, Player player, final int fIndex) {
        int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            int index = fIndex;
            for (int i = 0; i < 9; i++) {
                inv.setItem(slots[i], items.get(index + i));
            }
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
            index++;
            if (index + 1 > ticks.size()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 50, 50);
                for (int i = 0; i < 9; i++) {
                    if (i != 4) {
                        inv.setItem(slots[i], new ItemStack(Material.AIR));
                    }
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Ultime.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getCommand().replace("%player%", player.getName()));
                player.sendMessage("§aFélicitation §7tu as gagné \"" + inv.getItem(13).getItemMeta().getDisplayName() + "§7\" §edans ta Clé Ultime");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Main.getInstance().getOpeningKeys().remove(player);
                    player.closeInventory();
                }, 40);
                return;
            } else {
                scheduleUltime(inv, items, player, index);
            }
        }, ticks.get(fIndex));
    }

    public static void playVoteAnimation(Player player) {
        List<ItemStack> items = new ArrayList<>();
        for (Vote value : Vote.values()) {
            for (int i = 0; i < value.getProbability() * 2; i++) {
                items.add(new CustomItemStack(value.getItem()).setName(value.getName()));
            }
        }
        Collections.shuffle(items);
        Inventory inv = Bukkit.createInventory(null, 27, "§eClé Vote");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, CustomItemStack.getPane(14).setName("§7").setGlow(i == 4 || i == 22));
        }
        int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
        int avancement = 0;
        for (int i = 0; i < 9; i++) {
            inv.setItem(slots[i], items.get(avancement + i));
        }

        player.openInventory(inv);
        if (!Main.getInstance().getOpeningKeys().containsKey(player)) Main.getInstance().getOpeningKeys().put(player, inv);
        scheduleVote(inv, items, player, 0);
    }

    public static void scheduleVote(Inventory inv, final List<ItemStack> items, Player player, final int fIndex) {
        int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            int index = fIndex;
            for (int i = 0; i < 9; i++) {
                inv.setItem(slots[i], items.get(index + i));
            }
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
            index++;
            if (index + 1 > ticks.size()) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 50, 50);
                for (int i = 0; i < 9; i++) {
                    if (i != 4) {
                        inv.setItem(slots[i], new ItemStack(Material.AIR));
                    }
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Vote.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getCommand().replace("%player%", player.getName()));
                player.sendMessage("§aFélicitation §7tu as gagné \"" + inv.getItem(13).getItemMeta().getDisplayName() + "§7\" §edans ta Clé Vote");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Main.getInstance().getOpeningKeys().remove(player);
                    player.closeInventory();
                }, 40);
                return;
            } else {
                scheduleUltime(inv, items, player, index);
            }
        }, ticks.get(fIndex));
    }
}
