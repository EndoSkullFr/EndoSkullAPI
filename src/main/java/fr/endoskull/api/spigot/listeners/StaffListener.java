package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.spigot.utils.VanishUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public class StaffListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("endoskull.staff")) {
            Account account = AccountProvider.getAccount(player.getUniqueId());
            String vanished = account.getProperty("vanished", "false");
            if (vanished.equalsIgnoreCase("true")) {
                VanishUtils.vanish(player, true);
                e.setJoinMessage(null);
            } else if (VanishUtils.isVanished(player)) {
                VanishUtils.unvanish(player);
            }
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            if (VanishUtils.isVanished(onlinePlayer)) {
                VanishUtils.hidePlayer(onlinePlayer, player);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (!VanishUtils.isVanished(player)) return;
        if (!(e.getRightClicked() instanceof Player)) return;
        Player target = (Player) e.getRightClicked();
        if (player.getItemInHand() == null) return;
        switch (player.getItemInHand().getType()) {
            case AIR:
                break;
            case SLIME_BALL:
                player.performCommand("vulcan kb " + target.getName());
                break;
            case PACKED_ICE:
                player.performCommand("vulcan freeze " + target.getName());
                break;
            case SKULL_ITEM:
                player.performCommand("vulcan profile " + target.getName());
                break;
            case PAPER:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "forward " + player.getName() + " history " + target.getName());
                break;
            case CHEST:
                player.sendMessage("§cBientôt");
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        if (VanishUtils.isVanished(player)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        if (!VanishUtils.isVanished(player)) return;
        if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.STICK) {
            e.setCancelled(true);
            player.sendMessage("§cVous devez vous équiper du baton donner des coups");
            return;
        }
        e.setCancelled(false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (VanishUtils.isVanished(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (VanishUtils.isVanished(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (VanishUtils.isVanished(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (VanishUtils.isVanished(player)) e.setCancelled(true);
    }
}
