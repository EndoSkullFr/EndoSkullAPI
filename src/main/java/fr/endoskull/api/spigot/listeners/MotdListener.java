package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.MotdManager;
import fr.endoskull.api.spigot.inventories.motd.MotdPowerInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MotdListener implements Listener {

    @EventHandler
    public void onClickEdit(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (MotdManager.getWaitingLines().containsKey(player.getUniqueId())) {
            e.setCancelled(true);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                new MotdPowerInventory(e.getMessage().replace("&", "§"), MotdManager.getWaitingLines().get(player.getUniqueId()), 1).open(player);
                MotdManager.getWaitingLines().remove(player.getUniqueId());
            });
        }
    }
}
