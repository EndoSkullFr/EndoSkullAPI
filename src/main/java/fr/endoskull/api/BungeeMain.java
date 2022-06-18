package fr.endoskull.api;

import com.google.common.io.ByteStreams;
import fr.endoskull.api.bungee.commands.*;
import fr.endoskull.api.bungee.listeners.*;
import fr.endoskull.api.bungee.tasks.AnnouncmentTask;
import fr.endoskull.api.bungee.tasks.OnlineCountTask;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.LitebansHandler;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.api.commons.lang.Languages;
import litebans.api.Entry;
import litebans.api.Events;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeMain extends Plugin {
    private static BungeeMain instance;
    private HashMap<UUID, String> waitingServer = new HashMap<>();
    private HashMap<ProxiedPlayer, ProxiedPlayer> lastPM = new HashMap<>();

    private BasicDataSource connectionPool;
    public static String CHANNEL = "EndoSkullChannel";
    public static final String MESSAGE_CHANNEL = "commandforward:cmd";

    @Override
    public void onEnable() {
        instance = this;

        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        getProxy().registerChannel(CHANNEL);
        getProxy().registerChannel("PartiesChannel");
        getProxy().registerChannel(MESSAGE_CHANNEL);
        pm.registerListener(this, new CommandForwardListener());
        pm.registerListener(this, new ProxyPlayerListener());
        pm.registerListener(this, new ChatListener());
        pm.registerListener(this, new ForwardMessageListener(this));
        pm.registerListener(this, new ProxyPing());
        pm.registerListener(this, new CustomServerConnectListener());
        pm.registerListener(this, new ServerListener());
        //pm.registerListener(this, new PluginmessageListener(this));

        pm.registerCommand(this, new ForceCommand(this));
        pm.registerCommand(this, new OmgCommand());
        pm.registerCommand(this, new FriendCommand());
        pm.registerCommand(this, new PartyCommand());
        pm.registerCommand(this, new MaintenanceCommand());
        pm.registerCommand(this, new MsgCommand());
        pm.registerCommand(this, new ReplyCommand());
        pm.registerCommand(this, new ApiCommand());
        pm.registerCommand(this, new UnloadCommand());
        pm.registerCommand(this, new LobbyCommand());

        initConnection();
        new JedisAccess("127.0.0.1", 6379, "FimfvtAKApReX1kBgukpVn6CFyZLXa6X5MYXB4ZBFSnUqOfAd6pzqTi4GCrWcX7qwl8TSNUIMHR5MyIw").initConnection();

        getProxy().getScheduler().schedule(this, () -> {
            BungeeCord.getInstance().getScheduler().runAsync(this, PAFTask::run);
        }, 5, 5, TimeUnit.SECONDS);
        BungeeCord.getInstance().getScheduler().schedule(this, () -> {
            BungeeCord.getInstance().getScheduler().runAsync(this, JedisManager::sendToDatabase);
            }, 15, 15, TimeUnit.MINUTES);
        BungeeCord.getInstance().getScheduler().schedule(this, () -> {
            BungeeCord.getInstance().getScheduler().runAsync(this, new OnlineCountTask());
        }, 5, 5, TimeUnit.SECONDS);

        System.out.println("EndoSkullAPI Bungee ON");

        getProxy().getScheduler().schedule(this, new AnnouncmentTask(), 3, 15, TimeUnit.MINUTES);
        //((FriendList) Friends.getInstance().getSubCommand(FriendList.class)).registerTextReplacer(new FriendsTextReplacer());

        int port = 45900;
        for (ServerType value : ServerType.values()) {
            if (value.isRegisterServer()) {
                ServerInfo info = ProxyServer.getInstance().constructServerInfo(value.getServerName(), InetSocketAddress.createUnresolved("188.40.238.69", port), value.getServerName(), false);
                ProxyServer.getInstance().getServers().put(value.getServerName(), info);
                port++;
            }
        }
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                LitebansHandler.sendWebhookAdd(entry);
            }
        });
        Events.get().register(new Events.Listener() {
            @Override
            public void entryRemoved(Entry entry) {
                LitebansHandler.sendWebhookRemove(entry);
            }
        });

        for (Languages value : Languages.values()) {
            File file = new File(getDataFolder(), "languages/" + value.toString().toLowerCase() + ".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    try (InputStream is = getResourceAsStream("languages/" + value.toString().toLowerCase() + ".yml");
                         OutputStream os = new FileOutputStream(file)) {
                        ByteStreams.copy(is, os);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Unable to create configuration file", e);
                }
            }
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        JedisManager.sendToDatabase();
        JedisAccess.getUserpool().close();
        JedisAccess.getServerpool().close();
        super.onDisable();
    }

    public static BungeeMain getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return MySQL.getInstance();
    }

    private void initConnection(){
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
        connectionPool.setUsername("endoskull"); //w_512203
        connectionPool.setPassword("2M3hz44XRkkZkph3ExYqm2use5gJQG"); //45geFJ445geFJ445geFJ445geFJ4
        connectionPool.setUrl("jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "endoskull" + "?autoReconnect=true");
        connectionPool.setInitialSize(1);
        connectionPool.setMaxTotal(10);
        MySQL mysql = new MySQL(connectionPool);
        mysql.createTables();
        mysql.update("DELETE FROM `online_count` WHERE DATEDIFF(CURDATE(), `date`) > 7");
    }

    public HashMap<UUID, String> getWaitingServer() {
        return waitingServer;
    }

    public HashMap<ProxiedPlayer, ProxiedPlayer> getLastPM() {
        return lastPM;
    }
}
