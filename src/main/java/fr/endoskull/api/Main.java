package fr.endoskull.api;

import fr.endoskull.api.commands.*;
import fr.endoskull.api.database.MySQL;
import fr.endoskull.api.listeners.*;
import fr.endoskull.api.papi.EndoSkullPlaceholder;
import fr.endoskull.api.tasks.BossBarRunnable;
import fr.endoskull.api.tasks.PlayerCountTask;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Main instance;
    private HashMap<Player, Inventory> openingKeys = new HashMap<>();
    private HashMap<Player, Location> waitingSetting = new HashMap<>();

    private BasicDataSource connectionPool;
    private MySQL mysql;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageManager(this));
        initConnection();

        registerCommands();
        registerListeners();

        System.out.println("[" + getDescription().getName() + "] enable");
        if (getConfig().getBoolean("bossbar")) {
            Bukkit.getScheduler().runTaskTimer(this, new BossBarRunnable(this), 20L, 20L);
        }
        Bukkit.getScheduler().runTaskTimer(this, new PlayerCountTask(this), 20L, 20L);

        new EndoSkullPlaceholder().register();
        super.onEnable();
    }

    private void initConnection(){
        connectionPool = new BasicDataSource();
        connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
        connectionPool.setUsername(getConfig().getString("sql.user")); //w_512203
        connectionPool.setPassword(getConfig().getString("sql.password")); //45geFJ445geFJ445geFJ445geFJ4
        connectionPool.setUrl("jdbc:mysql://" + getConfig().getString("sql.host") + ":" + getConfig().getString("sql.port") + "/" + getConfig().getString("sql.database") + "?autoReconnect=true");
        connectionPool.setInitialSize(1);
        connectionPool.setMaxTotal(10);
        mysql = new MySQL(connectionPool);
        mysql.createTables();
    }

    private void registerCommands() {
        getCommand("level").setExecutor(new LevelCommand(this));
        getCommand("coins").setExecutor(new MoneyCommand(this));
        getCommand("boxset").setExecutor(new BoxSetCommand(this));
        getCommand("key").setExecutor(new KeyCommand(this));
        getCommand("boutique").setExecutor(new BoutiqueCommand());

        getCommand("endoskullapi").setExecutor(new EndoSkullApiCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerChat(this), this);
        pm.registerEvents(new ClickListener(this), this);
        pm.registerEvents(new PlayerInv(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new CustomGuiListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public HashMap<Player, Location> getWaitingSetting() {
        return waitingSetting;
    }

    public HashMap<Player, Inventory> getOpeningKeys() {
        return openingKeys;
    }

    public void reload() {
        reloadConfig();
        getCommand("rank").setPermission(getConfig().getString("rank-manage-permission"));
        getCommand("permission").setPermission(getConfig().getString("permission-manage-permission"));
    }
}
