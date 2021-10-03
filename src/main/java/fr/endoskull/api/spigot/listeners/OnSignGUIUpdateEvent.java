package fr.endoskull.api.spigot.listeners;

import com.antarescraft.kloudy.signguilib.SignGUIUpdateEvent;
import fr.endoskull.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnSignGUIUpdateEvent implements Listener {
    private Main main;

    public OnSignGUIUpdateEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void signUpdate(SignGUIUpdateEvent e)
    {
        Player player = e.getPlayer();
        if (main.getWaitingTag().containsKey(player.getUniqueId())) {
            String tag = e.getSignText()[0];
            if (tag == null || tag.length() < 1) {
                player.sendMessage("§cVous n'avez pas renseigné de tag");
                return;
            }
            if (tag.length() > 3) {
                tag = tag.substring(0, 3);
            }
            String finalTag = tag;
            Bukkit.getScheduler().runTask(main, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setsuffix &7[&" + main.getWaitingTag().get(player.getUniqueId()).getColor() + finalTag + "&7]");
                player.sendMessage("§aVotre tag a bien été défini");
                main.getWaitingTag().remove(player.getUniqueId());
            });
        }

    }
}
