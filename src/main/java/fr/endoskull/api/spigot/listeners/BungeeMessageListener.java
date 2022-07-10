package fr.endoskull.api.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.reports.MessagesLog;
import fr.endoskull.api.spigot.inventories.RequestsGui;
import fr.endoskull.api.spigot.inventories.SettingsGui;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (s.equals(Main.CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String sub = in.readUTF();
            if (sub.equals("OpenSettings")) {
                new SettingsGui(player).open(player);
            }
            if (sub.equals("OpenRequests")) {
                new RequestsGui(player, 0).open(player);
            }
            if (sub.equals("ServerTeleport")) {
                Account account = AccountProvider.getAccount(player.getUniqueId());
                if (!account.getProperty("serverTeleport", "").equalsIgnoreCase("")) {
                    Player target = Bukkit.getPlayer(account.getProperty("serverTeleport"));
                    account.setProperty("serverTeleport", "");
                    if (target != null) {
                        player.teleport(target);
                    }
                }
            }
            if (sub.equalsIgnoreCase("ReportShow")) {
                String senderName = in.readUTF();
                String targetName = in.readUTF();
                String reason = in.readUTF();
                String uuid = in.readUTF();
                TextComponent message = new TextComponent("§c§lReports §8» §e" + senderName + " §7vient de report §e" + targetName + " §7pour §e" + reason);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reports " + uuid));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eCliquez pour voir").create()));
                player.spigot().sendMessage(message);
            }
        }
    }
}
