package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ClickListener implements Listener {
    private Main main;
    public ClickListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClickEnt(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand) {
            ArmorStand as = (ArmorStand) e.getRightClicked();
            if (!as.isVisible() && !as.hasGravity()) e.setCancelled(true);
        }
    }
}
