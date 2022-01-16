package fr.endoskull.api.commons.server;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import fr.endoskull.api.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    /*public static String getServer(ServerType serverType, Player player) {
        PAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(player.getUniqueId());
        PlayerParty party = PartyManager.getInstance().getParty(pafPlayer);
        int playerNumber = 1;
        if (party != null) {
            if (party.isLeader(pafPlayer)) {
                playerNumber = party.getAllPlayers().size();
            }
        }
        ServiceInfoSnapshot server = null;
        for (String key : Main.getInstance().getJedisAccess().getServerpool().getResource().keys(serverType.getServerName() + "-*")) {
            if (Main.getInstance().getJedisAccess().getServerpool().getResource().get(key).equals(ServerState.ONLINE.toString())) {
                ServiceInfoSnapshot currentServer = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(serverType.getServerName()).stream().filter(serviceInfoSnapshot -> serviceInfoSnapshot.getName().equals(key)).findFirst().orElse(null);
                if (currentServer.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0) + playerNumber > serverType.getSemiFull()) continue;
                if (currentServer == null) {
                    System.out.println("Serveur " + key + " encore présent dans le cache alors que inexistant");
                    Main.getInstance().getJedisAccess().getServerpool().getResource().del(key);
                    continue;
                }
                if (server == null) {
                    server = currentServer;
                } else if (serverType.isLowIsBest() && ServiceInfoSnapshotUtil.getOnlineCount(server) > ServiceInfoSnapshotUtil.getOnlineCount(currentServer) || !serverType.isLowIsBest() && ServiceInfoSnapshotUtil.getOnlineCount(server) < ServiceInfoSnapshotUtil.getOnlineCount(currentServer)) {
                    server = currentServer;
                }
            }
        }
        if (server == null) return null;
        return server.getName();
    }*/
    public static String getServer(ServerType serverType, Player player, int playerNumber) {
        ServiceInfoSnapshot server = null;
        for (String key : Main.getInstance().getJedisAccess().getServerpool().getResource().keys(serverType.getServerName() + "-*")) {
            if (Main.getInstance().getJedisAccess().getServerpool().getResource().get(key).equals(ServerState.ONLINE.toString())) {
                ServiceInfoSnapshot currentServer = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(serverType.getServerName()).stream().filter(serviceInfoSnapshot -> serviceInfoSnapshot.getName().equals(key)).findFirst().orElse(null);
                if (currentServer.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0) + playerNumber > serverType.getSemiFull()) continue;
                if (currentServer == null) {
                    System.out.println("Serveur " + key + " encore présent dans le cache alors que inexistant");
                    Main.getInstance().getJedisAccess().getServerpool().getResource().del(key);
                    continue;
                }
                if (server == null) {
                    server = currentServer;
                } else if (serverType.isLowIsBest() && ServiceInfoSnapshotUtil.getOnlineCount(server) > ServiceInfoSnapshotUtil.getOnlineCount(currentServer) || !serverType.isLowIsBest() && ServiceInfoSnapshotUtil.getOnlineCount(server) < ServiceInfoSnapshotUtil.getOnlineCount(currentServer)) {
                    server = currentServer;
                }
            }
        }
        if (server == null) return null;
        return server.getName();
    }

}
