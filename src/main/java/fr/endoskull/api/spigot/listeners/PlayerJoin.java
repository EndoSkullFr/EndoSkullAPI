package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.data.sql.Keys;
import fr.endoskull.api.data.sql.Level;
import fr.endoskull.api.data.sql.Money;
import fr.endoskull.api.spigot.utils.EndoSkullPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PlayerJoin implements Listener {
    private Main main;
    public PlayerJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("UUID");
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //UUID uuid = PlayerInfos.getUuidFromName(player.getName());
            //if (!main.getUuidsByName().containsKey(player)) main.getUuidsByName().put(player.getName(), uuid);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
    }
}
