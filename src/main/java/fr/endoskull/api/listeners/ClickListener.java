package fr.endoskull.api.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.database.BoxLocation;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.keys.BoxInventory;
import fr.endoskull.api.utils.CustomItemStack;
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
        inv.setItem(12, new CustomItemStack(Material.ENDER_CHEST).setName("§4§LUltime"));
        inv.setItem(14, new CustomItemStack(Material.CHEST).setName("§eVote"));
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
        if (loc.equals(BoxLocation.getLoc("ULTIME"))) {
            e.setCancelled(true);
            if (main.getOpeningKeys().containsKey(player)) {
                player.openInventory(main.getOpeningKeys().get(player));
                return;
            }
            BoxInventory.openUltime(player);
        }
        if (loc.equals(BoxLocation.getLoc("VOTE"))) {
            e.setCancelled(true);
            if (main.getOpeningKeys().containsKey(player)) {
                player.openInventory(main.getOpeningKeys().get(player));
                return;
            }
            BoxInventory.openVote(player);
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
            e.setCancelled(true);
            if (e.getSlot() == 22 && current.getType().equals(Material.ANVIL)) {
                if (Keys.getKeys(main.getUuidsByName().get(player.getName()), "ultime") < 1) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                    player.sendMessage("§cVous devez posséder une §lClé Ultime §cpour effectuer cette action");
                    return;
                } else {
                    Keys.removeKey(main.getUuidsByName().get(player.getName()), "ultime", 1);
                    BoxInventory.playUltimeAnimation(player);
                }
            }
        }
        if (e.getClickedInventory().getName().equals("§eBox Vote")) {
            e.setCancelled(true);
            if (e.getSlot() == 22 && current.getType().equals(Material.ANVIL)) {
                if (Keys.getKeys(main.getUuidsByName().get(player.getName()), "vote") < 1) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 50, 50);
                    player.sendMessage("§cVous devez posséder une §lClé Vote §cpour effectuer cette action");
                    return;
                } else {
                    Keys.removeKey(main.getUuidsByName().get(player.getName()), "vote", 1);
                    BoxInventory.playVoteAnimation(player);
                }
            }
        }
    }
}
