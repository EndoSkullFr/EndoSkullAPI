package fr.endoskull.api.spigot.tasks;

import com.google.common.collect.Iterables;
import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.PluginMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerCountTask extends BukkitRunnable {
    private Main main;

    public PlayerCountTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null) return;
        updateServers(player);
        for (String server : PluginMessageManager.getServers()) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("PlayerList");
                out.writeUTF(server);
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Bukkit.broadcastMessage(PluginMessageManager.getServerCount().toString());
    }

    private void updateServers(Player player) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("GetServers");
            player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
