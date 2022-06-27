package fr.endoskull.api.spigot.tasks;

import fr.endoskull.api.spigot.utils.VanishUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffRunnable implements Runnable{
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (VanishUtils.isVanished(player)) VanishUtils.vanish(player, false);
        }
    }
}
