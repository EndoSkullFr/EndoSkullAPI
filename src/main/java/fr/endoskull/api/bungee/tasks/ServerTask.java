package fr.endoskull.api.bungee.tasks;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import fr.bebedlastreat.cache.data.redis.RedisManager;
import fr.endoskull.api.BungeeMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;

public class ServerTask implements Runnable {
    private BungeeMain main;

    public ServerTask(BungeeMain main) {
        this.main = main;
    }

    @Override
    public void run() {
        HashMap<UUID, String> wServer = new HashMap<>(main.getWaitingServer());
        wServer.forEach((uuid, s) -> {
            ProxiedPlayer player = main.getProxy().getPlayer(uuid);
            if (player != null) {
                String server = null;
                for (ServiceInfoSnapshot serviceInfoSnapshog : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(s)) {
                    String serviceName = serviceInfoSnapshog.getName();
                    String status = RedisManager.get("status/" + serviceName);
                    if (status == null) continue;
                    if (status.equalsIgnoreCase("LOBBY")) {
                        server = serviceName;
                        break;
                    }
                }
                if (server != null) {
                    player.connect(ProxyServer.getInstance().getServerInfo(server));
                    main.getWaitingServer().remove(uuid);
                }
            } else {
                main.getWaitingServer().remove(uuid);
            }
        });
    }
}
