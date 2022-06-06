package fr.endoskull.api.spigot.listeners;

import de.musterbukkit.replaysystem.main.ReplayAPI;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiCheatListener implements Listener {

    @EventHandler
    public void onPunish(VulcanPunishEvent e) {
        Player player = e.getPlayer();
        System.out.println("Event called");
        System.out.println(player.getName() + " got punish by vulcan");
        String snapshot = ReplayAPI.createSnapshotPlayer(120, player);
        System.out.println("Snapshot " + snapshot);
    }
}
