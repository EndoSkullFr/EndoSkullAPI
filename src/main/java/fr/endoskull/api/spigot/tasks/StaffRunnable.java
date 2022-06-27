package fr.endoskull.api.spigot.tasks;

import fr.endoskull.api.spigot.utils.VanishUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffRunnable implements Runnable{
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (VanishUtils.isVanished(player)) {
                ItemStack item = player.getInventory().getItem(1);
                if (item == null || item.getType() != Material.PACKED_ICE) {
                    VanishUtils.vanish(player, false);
                }
            }
        }
    }
}
