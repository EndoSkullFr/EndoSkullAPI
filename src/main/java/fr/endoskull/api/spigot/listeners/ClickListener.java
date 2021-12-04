package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.yaml.BoxLocation;
import fr.endoskull.api.spigot.inventories.KitKeyInventory;
import fr.endoskull.api.spigot.keys.BoxInventory;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickListener implements Listener {
    private Main main;
    public ClickListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClickEdit(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Player player = e.getPlayer();
        if (!player.isOp()) return;
        if (!main.getWaitingSetting().containsKey(player)) return;

        Inventory inv = Bukkit.createInventory(null, 27, "§aType de Box");
        inv.setItem(10, new CustomItemStack(Material.ENDER_CHEST).setName("§4§LUltime"));
        inv.setItem(12, new CustomItemStack(Material.CHEST).setName("§eVote"));
        inv.setItem(14, new CustomItemStack(Material.GOLD_INGOT).setName("§eCoins"));
        inv.setItem(16, new CustomItemStack(Material.IRON_CHESTPLATE).setName("§eKit"));
        inv.setItem(18, new CustomItemStack(Material.ARROW).setName("§7Retour"));
        inv.setItem(26, new CustomItemStack(Material.BARRIER).setName("§cAnnulé"));
        main.getWaitingSetting().put(player, e.getClickedBlock().getLocation());
        player.openInventory(inv);
    }

    @EventHandler
    public void onClickOpen(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        Location loc = e.getClickedBlock().getLocation();
        if (loc.equals(BoxLocation.getLocation("ULTIME"))) {
            e.setCancelled(true);
            if (main.getOpeningKeys().containsKey(player)) {
                player.openInventory(main.getOpeningKeys().get(player));
                return;
            }
            BoxInventory.openUltime(player);
        }
        if (loc.equals(BoxLocation.getLocation("VOTE"))) {
            e.setCancelled(true);
            if (main.getOpeningKeys().containsKey(player)) {
                player.openInventory(main.getOpeningKeys().get(player));
                return;
            }
            BoxInventory.openVote(player);
        }
        if (loc.equals(BoxLocation.getLocation("COINS"))) {
            e.setCancelled(true);
            if (main.getOpeningKeys().containsKey(player)) {
                player.openInventory(main.getOpeningKeys().get(player));
                return;
            }
            BoxInventory.openCoins(player);
        }
        if (loc.equals(BoxLocation.getLocation("KIT"))) {
            e.setCancelled(true);
            if (Bukkit.getPluginManager().getPlugin("EndoSkullPvpKit") != null) {
                if (main.getOpeningKeys().containsKey(player)) {
                    player.openInventory(main.getOpeningKeys().get(player));
                    return;
                }
                new KitKeyInventory(player).open(player);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;
        ItemStack current = e.getCurrentItem();
        if (current == null) return;
        if (e.getClickedInventory().getName().equals("§aType de Box")) {
            switch (current.getType()) {
                case ARROW:
                    player.closeInventory();
                    break;
                case BARRIER:
                    player.closeInventory();
                    main.getWaitingSetting().remove(player);
                    player.sendMessage("§cLa saisie du block a été annulé");
                    break;
                case ENDER_CHEST:
                    player.closeInventory();
                    BoxLocation.setLocation("ULTIME", main.getWaitingSetting().get(player));
                    player.sendMessage("§cLa location de la Box Ultime a bien été changé");
                    main.getWaitingSetting().remove(player);
                    break;
                case CHEST:
                    player.closeInventory();
                    BoxLocation.setLocation("VOTE", main.getWaitingSetting().get(player));
                    player.sendMessage("§cLa location de la Box Vote a bien été changé");
                    main.getWaitingSetting().remove(player);
                    break;
                case GOLD_INGOT:
                    player.closeInventory();
                    BoxLocation.setLocation("COINS", main.getWaitingSetting().get(player));
                    player.sendMessage("§cLa location de la Box Coins a bien été changé");
                    main.getWaitingSetting().remove(player);
                    break;
                case IRON_CHESTPLATE:
                    player.closeInventory();
                    BoxLocation.setLocation("KIT", main.getWaitingSetting().get(player));
                    player.sendMessage("§cLa location de la Box Kit a bien été changé");
                    main.getWaitingSetting().remove(player);
                    break;
                default:
                    break;
            }
        }
    }
    @EventHandler
    public void onClickKeyInv(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) return;
        ItemStack current = e.getCurrentItem();
        if (current == null) return;
        if (e.getClickedInventory().getName().equals("§4Box Ultime")) {
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            e.setCancelled(true);
            if (e.getSlot() == 22 && current.getType().equals(Material.ANVIL)) {
                if (account.getUltimeKey() < 1) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                    player.sendMessage("§cVous devez posséder une §lClé Ultime §cpour effectuer cette action");
                    return;
                } else {
                    account.setUltimeKey(account.getUltimeKey() - 1);
                    BoxInventory.playUltimeAnimation(player);
                }
            }
        }
        if (e.getClickedInventory().getName().equals("§eBox Vote")) {
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            e.setCancelled(true);
            if (e.getSlot() == 22 && current.getType().equals(Material.ANVIL)) {
                if (account.getVoteKey() < 1) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                    player.sendMessage("§cVous devez posséder une §lClé Vote §cpour effectuer cette action");
                    return;
                } else {
                    account.setVoteKey(account.getVoteKey() - 1);
                    BoxInventory.playVoteAnimation(player);
                }
            }
        }
        if (e.getClickedInventory().getName().equals("§eBox Coins")) {
            Account account = new AccountProvider(player.getUniqueId()).getAccount();
            e.setCancelled(true);
            if (e.getSlot() == 22 && current.getType().equals(Material.ANVIL)) {
                if (account.getCoinsKey() < 1) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                    player.sendMessage("§cVous devez posséder une §lClé Coins §cpour effectuer cette action");
                    return;
                } else {
                    account.setCoinsKey(account.getCoinsKey() - 1);
                    BoxInventory.playCoinsAnimation(player);
                }
            }
        }
    }
}
