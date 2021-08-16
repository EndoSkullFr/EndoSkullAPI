package fr.endoskull.api;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;

import java.util.*;

public class PluginMessageManager implements org.bukkit.plugin.messaging.PluginMessageListener {
    public PluginMessageManager(Main main) {
        this.main = main;
    }

    private Main main;
    private static HashMap<String, Integer> serverCount = new HashMap<>();
    private static List<String> servers = new ArrayList<>();

    public static HashMap<String, Integer> getServerCount() {
        return serverCount;
    }

    public static List<String> getServers() {
        return servers;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
            String subchannel = in.readUTF();
            if (subchannel.equals("PlayerCount")) {
                String server = in.readUTF();
                int playerCount = in.readInt();
                serverCount.put(server, playerCount);
            }
            if (subchannel.equals("GetServers")) {
                String[] serverList = in.readUTF().split(", ");
                servers = Arrays.asList(serverList);
            }
        }
    }

    public static int getPlayerCount(String server) {
        if (serverCount.containsKey(server)) {
            return serverCount.get(server);
        } else {
            return 0;
        }
    }
}
