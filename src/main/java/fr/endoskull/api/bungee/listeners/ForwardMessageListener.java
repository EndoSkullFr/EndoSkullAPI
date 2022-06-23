package fr.endoskull.api.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ForwardMessageListener implements Listener {
    private BungeeMain main;
    public ForwardMessageListener(BungeeMain main) {
        this.main = main;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase(BungeeMain.CHANNEL)) {
            ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
            String action = in.readUTF();
            if (action.equalsIgnoreCase("command")) {
                String command = in.readUTF();
                ProxiedPlayer player = (ProxiedPlayer) e.getReceiver();
                BungeeCord.getInstance().getPluginManager().dispatchCommand(player, command);
            }
            if (action.equalsIgnoreCase("UpdateLanguage")) {
                ProxiedPlayer player = (ProxiedPlayer) e.getReceiver();
                Account account = AccountProvider.getAccount(player.getUniqueId());
                BungeeMain.getLangs().put(player, account.getBungeeLang());
            }
            if (action.equalsIgnoreCase("ReportSend")) {
                String senderName = in.readUTF();
                String targetName = in.readUTF();
                String reason = in.readUTF();
                String uuid = in.readUTF();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    if (player.hasPermission("endoskull.seereports")) {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("ReportShow");
                        out.writeUTF(senderName);
                        out.writeUTF(targetName);
                        out.writeUTF(reason);
                        out.writeUTF(uuid);

                        player.getServer().sendData(BungeeMain.CHANNEL, out.toByteArray());
                    }
                }
            }
        }
    }
}
