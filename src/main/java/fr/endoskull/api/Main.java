package fr.endoskull.api;

import fr.endoskull.api.data.redis.RedisAccess;
import fr.endoskull.api.spigot.classement.ClassementTask;
import fr.endoskull.api.spigot.inventories.tag.TagColor;
import fr.endoskull.api.spigot.listeners.OnSignGUIUpdateEvent;
import fr.endoskull.api.spigot.PluginMessageManager;
import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.api.spigot.commands.*;
import fr.endoskull.api.spigot.listeners.*;
import fr.endoskull.api.spigot.papi.EndoSkullPlaceholder;
import fr.endoskull.api.spigot.tasks.BossBarRunnable;
import fr.endoskull.api.spigot.tasks.HologramTask;
import fr.endoskull.api.spigot.tasks.PlayerCountTask;
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
    private HashMap<UUID, TagColor> waitingTag = new HashMap<>();

    private BasicDataSource connectionPool;
    private MySQL mysql;
    private long load;

    @Override
    public void onLoad() {
        load = System.currentTimeMillis();
        super.onLoad();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageManager(this));

        initConnection();
        RedisAccess.init();

        registerCommands();
        registerListeners();

        System.out.println("[" + getDescription().getName() + "] enable");
        if (getConfig().getBoolean("bossbar")) {
            Bukkit.getScheduler().runTaskTimer(this, new BossBarRunnable(this), 20L, 20L);
        }
        Bukkit.getScheduler().runTaskTimer(this, new PlayerCountTask(this), 20L, 20L);

        new EndoSkullPlaceholder().register();

        if (Bukkit.getPluginManager().getPlugin("DeluxeHub_EndoSkull") != null) {
            Bukkit.getScheduler().runTaskTimer(this, new HologramTask(), 100, 100);
        }
        if (Bukkit.getPluginManager().getPlugin("EndoSkullPvpKit") != null) {
            Bukkit.getScheduler().runTaskTimer(this, new ClassementTask(), 100, 100);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        RedisAccess.close();
        super.onDisable();
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
        getCommand("booster").setExecutor(new BoosterCommand(this));
        getCommand("boxset").setExecutor(new BoxSetCommand(this));
        getCommand("key").setExecutor(new KeyCommand(this));
        getCommand("boutique").setExecutor(new BoutiqueCommand());
        getCommand("discord").setExecutor(new LinkCommand());
        getCommand("lobby").setExecutor(new ServerCommand(this));
        getCommand("tag").setExecutor(new TagCommand(this));
        getCommand("load").setExecutor(new LoadCommand(this));

        getCommand("endoskullapi").setExecutor(new EndoSkullApiCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerChat(this), this);
        pm.registerEvents(new ClickListener(this), this);
        pm.registerEvents(new PlayerInv(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new CustomGuiListener(), this);
        pm.registerEvents(new OnSignGUIUpdateEvent(this), this);
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
    }

    public long getLoad() {
        return load;
    }

    public HashMap<UUID, TagColor> getWaitingTag() {
        return waitingTag;
    }
}
