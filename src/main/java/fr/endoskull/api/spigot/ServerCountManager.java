package fr.endoskull.api.spigot;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.Main;
import fr.endoskull.api.spigot.utils.ServerType;
import org.bukkit.entity.Player;

import java.util.*;

public class ServerCountManager implements org.bukkit.plugin.messaging.PluginMessageListener {
    public ServerCountManager(Main main) {
        this.main = main;
    }

    private Main main;
    private static HashMap<String, Integer> serverCount = new HashMap<>();
    private static List<String> servers = new ArrayList<>();

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

    public static int getPlayerCount(ServerType serverType) {
        int count = 0;
        for (String s : serverCount.keySet()) {
            if (s.startsWith(serverType.getServerName())) {
                count += serverCount.get(s);
            }
        }
        return count;
    }
}