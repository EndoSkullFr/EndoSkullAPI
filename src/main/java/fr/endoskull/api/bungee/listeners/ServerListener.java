package fr.endoskull.api.bungee.listeners;

import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.data.sql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class ServerListener implements Listener {

    @EventHandler
    public void onServerChange(ServerConnectedEvent e) {
        ProxyServer.getInstance().getScheduler().schedule(BungeeMain.getInstance(), () -> {
            for (String task : new String[]{"Lobby", "PvpKit", "Bedwars"}) {
                final int onlineCount = ServiceInfoSnapshotUtil.getTaskOnlineCount(task);
                MySQL.getInstance().update("INSERT INTO `online_count`(`service`, `online`) VALUES ('" + task + "','" + onlineCount + "')");
            }
        }, 1, TimeUnit.SECONDS);
    }
}
