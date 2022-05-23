package fr.endoskull.api.commons.server;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.ServiceInfoSnapshotUtil;
import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.JedisAccess;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    public static String getServer(ServerType serverType, int playerNumber) {
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();
            ServiceInfoSnapshot server = null;
            for (String key : j.keys(serverType.getServerName() + "-*")) {
                if (j.get(key).equals(ServerState.ONLINE.toString())) {
                    ServiceInfoSnapshot currentServer = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(serverType.getServerName()).stream().filter(serviceInfoSnapshot -> serviceInfoSnapshot.getName().equals(key)).findFirst().orElse(null);
                    if (currentServer == null) {
                        System.out.println("Serveur " + key + " encore présent dans le cache alors que inexistant");
                        j.del(key);
                        continue;
                    }
                    if (Integer.parseInt(j.get("online/" + currentServer.getName())) + playerNumber > serverType.getSemiFull()) continue;
                    if (server == null) {
                        server = currentServer;
                    } else if (serverType.isLowIsBest() && Integer.parseInt(j.get("online/" + server.getName())) > Integer.parseInt(j.get("online/" + currentServer.getName())) || !serverType.isLowIsBest() && Integer.parseInt(j.get("online/" + server.getName())) < Integer.parseInt(j.get("online/" + currentServer.getName()))) {
                        server = currentServer;
                    }
                }
            }
            if (server == null) return null;
            return server.getName();
        } finally {
            j.close();
        }

    }

}
