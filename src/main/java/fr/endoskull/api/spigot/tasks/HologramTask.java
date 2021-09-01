package fr.endoskull.api.spigot.tasks;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import fr.endoskull.api.spigot.PluginMessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class HologramTask implements Runnable {
    private Hologram pvpHologram;

    @Override
    public void run() {
        int pvpConnect = 0;
        if (PluginMessageManager.getServerCount().containsKey("PvpKit")) {
            pvpConnect = PluginMessageManager.getServerCount().get("PvpKit");
        }
        if (pvpHologram == null) {
            pvpHologram = HologramAPI.createHologram(new Location(Bukkit.getWorld("world"), -246.5, 65.25, -253.5), "§eConnecté(s): §f" + pvpConnect);
            pvpHologram.spawn();
        }
        pvpHologram.setText("§eConnecté(s): §f" + pvpConnect);
    }
}
