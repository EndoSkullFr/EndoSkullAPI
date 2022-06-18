package fr.endoskull.api.bungee.listeners;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.Party;
import fr.endoskull.api.commons.paf.PartyUtils;
import fr.endoskull.api.commons.server.ServerManager;
import fr.endoskull.api.commons.server.ServerType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CustomServerConnectListener implements Listener {
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry()
            .getFirstService(IPlayerManager.class);

    @EventHandler
    public void onServerConnect(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        ServerInfo serverInfo = e.getTarget();
        if (serverInfo.getName().equalsIgnoreCase("fallback")) {
            String server = ServerManager.getServer(ServerType.LOBBY, 1);
            if (server == null) {
                e.setCancelled(true);
                player.disconnect(new TextComponent(BungeeLang.getLang(player).getMessage(MessageUtils.Global.ANY_LOBBY)));
                return;
            }
            e.setTarget(ProxyServer.getInstance().getServerInfo(server));
            //playerManager.getPlayerExecutor(player.getUniqueId()).connect(server);
            return;
        }
        for (ServerType value : ServerType.values()) {
            if (value.isRegisterServer()) {
                if (serverInfo.getName().equalsIgnoreCase(value.getServerName())) {
                    int partySize = 1;
                    if (PartyUtils.isInParty(player.getUniqueId())) {
                        Party party = PartyUtils.getParty(player.getUniqueId());
                        if (party.getLeader().equals(player.getUniqueId())) {
                            partySize = party.getPlayers().size();
                        }
                    }
                    String server = ServerManager.getServer(value, partySize);
                    if (server == null) {
                        e.setCancelled(true);
                        player.sendMessage(BungeeLang.getLang(player).getMessage(MessageUtils.Global.ANY_SERVER));
                        return;
                    }
                    e.setTarget(ProxyServer.getInstance().getServerInfo(server));
                    //playerManager.getPlayerExecutor(player.getUniqueId()).connect(server);
                }
            }
        }
    }

    @EventHandler
    public void onKick(ServerKickEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (e.getKickedFrom().getName().equalsIgnoreCase("fallback")) return;
        String server = ServerManager.getServer(ServerType.LOBBY, 1);
        if (server == null) {
            return;
        }
        Server ser = player.getServer();
        if (ser != null && ser.getInfo() != null) {
            if (ser.getInfo().getName().equalsIgnoreCase(server)) {
                player.disconnect(e.getKickReasonComponent());
                return;
            }
        }
        e.setCancelled(true);
        e.setCancelServer(ProxyServer.getInstance().getServerInfo("fallback"));
        player.sendMessage(e.getKickReasonComponent());
    }
}
