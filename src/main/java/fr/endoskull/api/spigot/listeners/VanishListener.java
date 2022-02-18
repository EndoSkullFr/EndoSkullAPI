package fr.endoskull.api.spigot.listeners;

import de.myzelyam.api.vanish.PlayerVanishStateChangeEvent;
import de.myzelyam.api.vanish.VanishAPI;
import fr.bebedlastreat.cache.CacheAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    @EventHandler
    public void onVanish(PlayerVanishStateChangeEvent e) {
        Player player = Bukkit.getPlayer(e.getUUID());
        if (player == null) return;
        if (e.isVanishing()) {
            CacheAPI.set("vanish/" + player.getUniqueId(), 1);
        } else {
            CacheAPI.remove("vanish/" + player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (CacheAPI.keyExist("vanish/" + player.getUniqueId())) {
            VanishAPI.hidePlayer(player);
            e.setJoinMessage(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (CacheAPI.keyExist("vanish/" + player.getUniqueId())) {
            VanishAPI.showPlayer(player);
            e.setQuitMessage(null);
        }
    }
}
