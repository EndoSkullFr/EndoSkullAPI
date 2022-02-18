package fr.endoskull.api.spigot.keys;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.pvpkit.kits.Kit;
import fr.endoskull.pvpkit.kits.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoxInventory {


    public static void openVote(Player player) {
        Account account = new AccountProvider(player.getUniqueId()).getAccount();
        Inventory inv = Bukkit.createInventory(null, 27, "§eBox Vote");
        int i = 0;
        for (Vote value : Vote.values()) {
            inv.setItem(i, new CustomItemStack(value.getItem()).setName(value.getName()).setLore("\n§7Probabilité: " + value.getName().substring(0, 2) + value.getPourcent() + "%"));
            i++;
        }
        inv.setItem(22, new CustomItemStack(Material.ANVIL).setName("§6Ouvrir").setLore("\n§7Vous avez §6" + account.getVoteKey() + " §7Clé(s) Vote(s)"));
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 50, 50);
    }

    public static void openCoins(Player player) {
        Account account = new AccountProvider(player.getUniqueId()).getAccount();
        Inventory inv = Bukkit.createInventory(null, 27, "§eBox Coins");
        int i = 0;
        for (Coins value : Coins.values()) {
            inv.setItem(i, new CustomItemStack(value.getItem()).setName(value.getName()).setLore("\n§7Probabilité: " + value.getName().substring(0, 2) + value.getPourcent() + "%"));
            i++;
        }
        inv.setItem(22, new CustomItemStack(Material.ANVIL).setName("§6Ouvrir").setLore("\n§7Vous avez §6" + account.getCoinsKey() + " §7Clé(s) Coins"));
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 50, 50);
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

        for (int i = 0; i < 9; i++) {
            inv.setItem(9 + i, items.get(i));
        }

        player.openInventory(inv);
        //if (!Main.getInstance().getOpeningKeys().containsKey(player)) Main.getInstance().getOpeningKeys().put(player, inv);
        scheduleVote(inv, items, player, 1);
    }

    public static void scheduleVote(Inventory inv, final List<ItemStack> items, Player player, final int fIndex) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            int index = fIndex;
            for (int i = 0; i < 9; i++) {
                inv.setItem(9 + i, items.get(index + i));
            }

            index++;
            if (index > 50) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 50, 50);
                System.out.println("Clé vote " + player.getName() + " " + Vote.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Vote.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getCommand().replace("%player%", player.getName()));
                player.sendMessage("§aFélicitation §etu as gagné §7\"§e" + inv.getItem(13).getItemMeta().getDisplayName() + "§7\" §edans ta Clé Vote");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Main.getInstance().getOpeningKeys().remove(player);
                    player.closeInventory();
                }, 40);
                return;
            } else {
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                scheduleVote(inv, items, player, index);
            }
        }, Math.round(Math.pow(Math.pow(Math.pow(1.000018, fIndex), fIndex), fIndex)));
    }

    public static void playCoinsAnimation(Player player) {
        List<ItemStack> items = new ArrayList<>();
        for (Coins value : Coins.values()) {
            for (int i = 0; i < value.getProbability() * 2; i++) {
                items.add(new CustomItemStack(value.getItem()).setName(value.getName()));
            }
        }
        Collections.shuffle(items);
        Inventory inv = Bukkit.createInventory(null, 27, "§eClé Coins");
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, CustomItemStack.getPane(14).setName("§7").setGlow(i == 4 || i == 22));
        }

        for (int i = 0; i < 9; i++) {
            inv.setItem(9 + i, items.get(i));
        }

        player.openInventory(inv);
        //if (!Main.getInstance().getOpeningKeys().containsKey(player)) Main.getInstance().getOpeningKeys().put(player, inv);
        scheduleCoins(inv, items, player, 1);
    }

    public static void scheduleCoins(Inventory inv, final List<ItemStack> items, Player player, final int fIndex) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            int index = fIndex;
            for (int i = 0; i < 9; i++) {
                inv.setItem(9 + i, items.get(index + i));
            }

            index++;
            if (index > 50) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 50, 50);
                System.out.println("Clé Coins " + player.getName() + " " + Coins.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Coins.getByName(inv.getItem(13).getItemMeta().getDisplayName()).getCommand().replace("%player%", player.getName()));
                player.sendMessage("§aFélicitation §etu as gagné §7\"§e" + inv.getItem(13).getItemMeta().getDisplayName() + "§7\" §edans ta Clé Coins");
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Main.getInstance().getOpeningKeys().remove(player);
                    player.closeInventory();
                }, 40);
                return;
            } else {
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
                scheduleCoins(inv, items, player, index);
            }
        }, Math.round(Math.pow(Math.pow(Math.pow(1.000018, fIndex), fIndex), fIndex)));
    }

    public static void giveKit(Player player) {
        List<Kit> kitList = new ArrayList<>(KitManager.getTier1());
        player.closeInventory();
        if (player.hasPermission("group.vip")) kitList.addAll(KitManager.getTier2());
        if (player.hasPermission("group.hero")) kitList.addAll(KitManager.getTier3());
        Kit kit = kitList.get(new Random().nextInt(kitList.size()));
        player.sendMessage("§aFélicitation §etu as gagné le §a§lKit " + kit.getDisplayName() + " §edans ta Clé kit");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 50, 50);
        System.out.println("Clé Kit " + player.getName() + " " + kit.getName());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit add " + player.getName() + " " + kit.getName());
    }
}
