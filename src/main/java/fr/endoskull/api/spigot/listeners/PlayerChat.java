package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

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
            String suffix = user.getCachedData().getMetaData().getSuffix();
            if (suffix == null) suffix = "";
            String format = main.getConfig().getString("chatmsg");
            format = format.replace("%prefix%", prefix);
            format = format.replace("%suffix%", suffix);
            format = format.replace("&", "§");
            e.setFormat(format);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("endoskull.seeplugins")) return;
        String command = e.getMessage();
        command = command.replace("/", "");
        if (command.contains(" ")) {
            command = command.split(" ")[0];
        }
        if (command.equalsIgnoreCase("bukkit:pl") || command.equalsIgnoreCase("bukkit:plugins") || command.equals("pl") || command.equalsIgnoreCase("plugins")) {
            e.setCancelled(true);
            List<String> plugins = new ArrayList<>();
            for (Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
                if (plugin.getDescription().getAuthors().contains("BebeDlaStreat")) {
                    plugins.add(plugin.getName());
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append("§fPlugins (" + plugins.size() + "): ");
            for (String plugin : plugins) {
                builder.append("§a" + plugin);
                if (plugins.indexOf(plugin) + 1 != plugins.size()) builder.append("§f, ");
            }
            player.sendMessage(builder.toString());
        }
    }
}
