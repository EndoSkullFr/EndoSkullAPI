package fr.endoskull.api;

import fr.endoskull.api.bungee.commands.ForceCommand;
import fr.endoskull.api.bungee.commands.OmgCommand;
import fr.endoskull.api.bungee.listeners.ForwardMessageListener;
import fr.endoskull.api.bungee.listeners.PluginmessageListener;
import fr.endoskull.api.bungee.listeners.ProxyPing;
import fr.endoskull.api.bungee.listeners.ProxyPlayerListener;
import fr.endoskull.api.bungee.tasks.AnnouncmentTask;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.data.redis.RedisAccess;
import fr.endoskull.api.data.redis.RedisCredentials;
import fr.endoskull.api.data.sql.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.apache.commons.dbcp2.BasicDataSource;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeMain extends Plugin {
    private static BungeeMain instance;
    private HashMap<UUID, String> waitingServer = new HashMap<>();

    private BasicDataSource connectionPool;
    private MySQL mysql;
    public String CHANNEL = "EndoSkullChannel";

    @Override
    public void onEnable() {
        instance = this;

        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        getProxy().registerChannel(CHANNEL);
        getProxy().registerChannel("PartiesChannel");
        pm.registerListener(this, new ProxyPlayerListener());
        pm.registerListener(this, new ProxyPing());
        pm.registerListener(this, new ForwardMessageListener(this));
        //pm.registerListener(this, new PluginmessageListener(this));

        pm.registerCommand(this, new ForceCommand(this));
        pm.registerCommand(this, new OmgCommand());

        initConnection();
        RedisAccess.init();
        new JedisAccess("127.0.0.1", 6379, "%]h48Ty7UBC?D+439zg%XeV6Pm#k~&9y").initConnection();

        BungeeCord.getInstance().getScheduler().schedule(this, () -> {
            BungeeCord.getInstance().getScheduler().runAsync(this, () -> {
                RedisAccess.sendToDatabase();
            });
            }, 15, 15, TimeUnit.MINUTES);

        System.out.println("EndoSkullAPI Bungee ON");

        getProxy().getScheduler().schedule(this, new AnnouncmentTask(), 3, 15, TimeUnit.MINUTES);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        RedisAccess.sendToDatabase();
        RedisAccess.close();
        super.onDisable();
    }

    public static BungeeMain getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mysql;
    }

    private void initConnection(){
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
        connectionPool.setUsername("endoskull"); //w_512203
        connectionPool.setPassword("9zRQ2Cb03DxdPTmG"); //45geFJ445geFJ445geFJ445geFJ4
        connectionPool.setUrl("jdbc:mysql://" + "localhost" + ":" + "3306" + "/" + "endoskull" + "?autoReconnect=true");
        connectionPool.setInitialSize(1);
        connectionPool.setMaxTotal(10);
        mysql = new MySQL(connectionPool);
        mysql.createTables();
    }

    public HashMap<UUID, String> getWaitingServer() {
        return waitingServer;
    }
}
