package fr.endoskull.api.spigot.tasks;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import fr.endoskull.api.spigot.ServerCountManager;
import fr.endoskull.api.spigot.utils.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HologramTask implements Runnable {
    private Hologram pvpHologram;

    @Override
    public void run() {
        int pvpConnect = ServerCountManager.getPlayerCount(ServerType.PVPKIT);
        if (pvpHologram == null) {
            pvpHologram = HologramAPI.createHologram(new Location(Bukkit.getWorld("world"), -246.5, 65.25, -253.5), "§eConnecté(s): §f" + pvpConnect);
            pvpHologram.spawn();
        }
        pvpHologram.setText("§eConnecté(s): §f" + pvpConnect);
    }
}
