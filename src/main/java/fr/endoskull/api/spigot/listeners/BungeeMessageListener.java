package fr.endoskull.api.spigot.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.spigot.inventories.RequestsGui;
import fr.endoskull.api.spigot.inventories.SettingsGui;
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
        }
    }
}
