package fr.endoskull.api.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.utils.RankApi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {
    private Main main;
    public PlayerChat(Main main) {
        this.main = main;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (main.getConfig().getBoolean("enablechat")) {
            String format = main.getConfig().getString("chatmsg");
            format = format.replace("%rank%", RankApi.getRank(main.getUuidsByName().get(player.getName())).getPrefix());
            format = format.replace("&", "§");
            e.setFormat(format);
        }
    }
}
