package fr.endoskull.api;

import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.server.ServerState;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.spigot.classement.ClassementTask;
import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.api.spigot.commands.*;
import fr.endoskull.api.spigot.listeners.*;
import fr.endoskull.api.spigot.tasks.StaffRunnable;
import fr.endoskull.api.spigot.utils.AntiTabComplete;
import fr.endoskull.api.spigot.utils.Languages;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends JavaPlugin {
    private static Main instance;
    private JedisAccess jedisAccess;
    public static final String CHANNEL = "EndoSkullChannel";
    public static final String MESSAGE_CHANNEL = "commandforward:cmd";
    public final String PLUGIN_NAME_PREFIX = ChatColor.DARK_AQUA + "[" + ChatColor.GOLD + this.getName() + ChatColor.DARK_AQUA + "] ";
    private ServerType serverType;
    private static final HashMap<Player, Languages> langs = new HashMap<>();
    private static final HashMap<Languages, YamlConfiguration> langFiles = new HashMap<>();

    private BasicDataSource connectionPool;
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
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "PartiesChannel");
        getServer().getMessenger().registerOutgoingPluginChannel(this, MESSAGE_CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, new BungeeMessageListener());

        initConnection();
        jedisAccess = new JedisAccess("127.0.0.1", 6379, "FimfvtAKApReX1kBgukpVn6CFyZLXa6X5MYXB4ZBFSnUqOfAd6pzqTi4GCrWcX7qwl8TSNUIMHR5MyIw");
        jedisAccess.initConnection();

        registerCommands();
        registerListeners();

        System.out.println("[" + getDescription().getName() + "] enable");

        //new EndoSkullPlaceholder().register();
        //new CloudNetExpansion().register();

        Bukkit.getScheduler().runTaskTimer(this, new StaffRunnable(), 20, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ClassementTask(this), 100, 20 * 20);

        createServerFile();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getServerName().contains("-")) {
                    Jedis j = null;
                    try {
                        j = JedisAccess.getServerpool().getResource();
                        if (ServerType.getByName(Bukkit.getServerName().split("-")[0]) != null) {
                            serverType = ServerType.getByName(Bukkit.getServerName().split("-")[0]);
                            j.set(Bukkit.getServerName(), ServerState.ONLINE.toString());
                            if (!serverType.isMultiArena()) {
                                j.set("online/" + Bukkit.getServerName(), String.valueOf(Bukkit.getOnlinePlayers().size()));
                            }
                            System.out.println("[EndoSkull] Ajout du serveur dans le redis, prêt à recevoir des joueurs");
                        } else {
                            serverType = ServerType.UNKNOW;
                            System.out.println("[EndoSkull] Type de serveur inconnu");
                        }
                    } finally {
                        j.close();
                    }

                }
            }
        }.runTaskLaterAsynchronously(this, 3 * 20);

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            new AntiTabComplete();
        }

        for (Languages value : Languages.values()) {
            saveResource("languages/" + value.toString().toLowerCase() + ".yml", false);
            langFiles.put(value, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "languages/" + value.toString().toLowerCase() + ".yml")));
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.kickPlayer(langs.get(pls).getMessage(MessageUtils.Global.SERVER_STOP));
        }
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();

            j.del(Bukkit.getServerName());
            j.del("online/" + Bukkit.getServerName());
        } finally {
            j.close();
        }
        JedisAccess.getServerpool().close();
        JedisAccess.getUserpool().close();
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
        MySQL mysql = new MySQL(connectionPool);
        mysql.createTables();
    }

    private void registerCommands() {
        getCommand("level").setExecutor(new LevelCommand(this));
        getCommand("coins").setExecutor(new MoneyCommand(this));
        getCommand("boutique").setExecutor(new BoutiqueCommand());
        getCommand("discord").setExecutor(new LinkCommand());
        getCommand("load").setExecutor(new LoadCommand(this));
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("sound").setExecutor(new SoundCommand());
        getCommand("endoskull").setExecutor(new EndoSkullCommand(this));
        getCommand("forward").setExecutor(new ForwardCommand(this));
        getCommand("booster").setExecutor(new BoosterCommand(this));
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("about").setExecutor(new AboutCommand());
        getCommand("sanction").setExecutor(new SanctionCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reports").setExecutor(new ReportsCommand());
        getCommand("key").setExecutor(new KeyCommand());

        getCommand("endoskullapi").setExecutor(new EndoSkullApiCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerChat(this), this);
        pm.registerEvents(new ClickListener(this), this);
        pm.registerEvents(new PlayerInv(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new CustomGuiListener(), this);
        pm.registerEvents(new StaffListener(), this);

        System.out.println(Bukkit.getPluginManager().getPlugin("Vulcan") + " " +  Bukkit.getPluginManager().getPlugin("ReplaySystem"));
        if (Bukkit.getPluginManager().getPlugin("Vulcan") != null && Bukkit.getPluginManager().getPlugin("ReplaySystem") != null) {
            pm.registerEvents(new AntiCheatListener(), this);
            System.out.println("Event register");
        }
    }

    private void createServerFile() {
        File file = new File(getDataFolder(), "server.yml");

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            saveResource("server.yml", false);
        }
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


    }

    public static Main getInstance() {
        return instance;
    }

    public MySQL getMySQL() {
        return MySQL.getInstance();
    }

    public void reload() {
        reloadConfig();
    }

    public long getLoad() {
        return load;
    }

    public JedisAccess getJedisAccess() {
        return jedisAccess;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public static HashMap<Player, Languages> getLangs() {
        return langs;
    }
    public static HashMap<Languages, YamlConfiguration> getLangFiles() {
        return langFiles;
    }

    public void reloadLangFiles() {
        for (Languages value : Languages.values()) {
            langFiles.put(value, YamlConfiguration.loadConfiguration(new File(getDataFolder(), "languages/" + value.toString().toLowerCase() + ".yml")));
        }
    }

}
