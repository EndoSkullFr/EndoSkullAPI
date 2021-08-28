package fr.endoskull.api.listeners;

import fr.endoskull.api.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
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
            /*String format = main.getConfig().getString("chatmsg");
            format = format.replace("%rank%", RankApi.getRank(main.getUuidsByName().get(player.getName())).getPrefix());
            format = format.replace("&", "§");
            e.setFormat(format);*/
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
            String prefix = user.getCachedData().getMetaData().getPrefix();
            if (prefix == null) prefix = "§7";
            prefix = prefix.substring(0, 2);
            String suffix = user.getCachedData().getMetaData().getSuffix();
            if (suffix == null) suffix = "";
            String format = main.getConfig().getString("chatmsg");
            format = format.replace("%prefix%", prefix);
            format = format.replace("%suffix%", suffix);
            format = format.replace("&", "§");
            e.setFormat(format);
        }
    }
}
