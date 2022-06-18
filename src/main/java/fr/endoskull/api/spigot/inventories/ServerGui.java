package fr.endoskull.api.spigot.inventories;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.server.ServerState;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerGui extends CustomGui {
    private Integer[] glass = {0,1,2,3,4,5,6,7,8,9,18,27,36,45,17,26,35,44,53};
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry()
            .getFirstService(IPlayerManager.class);

    public ServerGui(ServerType serverType, Main main, Player p) {
        super(getLines(serverType), Languages.getLang(p).getMessage(MessageUtils.Global.GUI_SERVERS), p);
        Languages lang = Languages.getLang(p);
        for (int i : glass) {
            if (i >= getLine() * 9) continue;
            setItem(i, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName("§r"));
        }
        for (int i = getLine() * 9 - 9; i < getLine() * 9; i++) {
            setItem(i, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7).setName("§r"));
        }

        Jedis jedis = null;
        try {
            jedis = JedisAccess.getServerpool().getResource();
            List<String> servers = new ArrayList<>(jedis.keys(serverType.getServerName() + "-*"));
            Collections.sort(servers);
            int i = 0;
            for (String server : servers) {
                while (Arrays.asList(glass).contains(i)) i++;
                ServerState serverState = ServerState.valueOf(jedis.get(server));
                ServiceInfoSnapshot serverInfo = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(serverType.getServerName()).stream().filter(serviceInfoSnapshot -> serviceInfoSnapshot.getName().equals(server)).findFirst().orElse(null);
                setItem(i, new CustomItemStack(Material.WOOL, serverInfo.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0), (byte) (serverState == ServerState.ONLINE ? 5 : 14)).setName("§e§l" + server)
                        .setLore("\n§7" + lang.getMessage(MessageUtils.Global.STATE) + "§8» " + serverState.getDisplayName() + "\n" +
                                "§7" + lang.getMessage(MessageUtils.Global.PLAYERS) + " §8» §f" + serverInfo.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0) + "§7/§f" + serverInfo.getProperty(BridgeServiceProperty.MAX_PLAYERS).orElse(0)), player -> {
                    playerManager.getPlayerExecutor(player.getUniqueId()).connect(server);
                });
                i++;
            }
        } finally {
            jedis.close();
        }
    }

    private static int getLines(ServerType serverType) {
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();
            return (j.keys(serverType.getServerName() + "-*").size() - 1) / 7 + 3;
        } finally {
            j.close();
        }
    }
}
