package fr.endoskull.api;

import fr.endoskull.api.bungee.listeners.ProxyPlayerListener;
import fr.endoskull.api.data.redis.RedisAccess;
import fr.endoskull.api.data.sql.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.concurrent.TimeUnit;

public class BungeeMain extends Plugin {
    private static BungeeMain instance;

    private BasicDataSource connectionPool;
    private MySQL mysql;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, new ProxyPlayerListener());

        initConnection();
        RedisAccess.init();

        BungeeCord.getInstance().getScheduler().schedule(this, () -> {
            BungeeCord.getInstance().getScheduler().runAsync(this, () -> {
                RedisAccess.sendToDatabase();
            });
            }, 15, TimeUnit.MINUTES);

        System.out.println("EndoSkullAPI Bungee ON");
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
}
