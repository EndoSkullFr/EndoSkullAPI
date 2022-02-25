package fr.endoskull.api;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import fr.endoskull.api.commons.server.ServerState;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.spigot.classement.ClassementTask;
import fr.endoskull.api.spigot.inventories.tag.TagColor;
import fr.endoskull.api.data.sql.MySQL;
import fr.endoskull.api.spigot.commands.*;
import fr.endoskull.api.spigot.listeners.*;
import fr.endoskull.api.spigot.papi.CloudNetExpansion;
import fr.endoskull.api.spigot.papi.EndoSkullPlaceholder;
import fr.endoskull.api.spigot.tasks.BossBarRunnable;
import fr.endoskull.api.spigot.utils.CustomGui;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Main extends JavaPlugin {
    private static Main instance;
    private JedisAccess jedisAccess;
    private HashMap<Player, CustomGui> openingKeys = new HashMap<>();
    private HashMap<Player, Location> waitingSetting = new HashMap<>();
    private HashMap<UUID, TagColor> waitingTag = new HashMap<>();
    public String CHANNEL = "EndoSkullChannel";
    public static final String MESSAGE_CHANNEL = "commandforward:cmd";
    public final String PLUGIN_NAME_PREFIX = ChatColor.DARK_AQUA + "[" + ChatColor.GOLD + this.getName() + ChatColor.DARK_AQUA + "] ";
    private ServerType serverType;

    private BasicDataSource connectionPool;
    private MySQL mysql;
    private long load;
    private HashMap<UUID, String> nicks = new HashMap<>();

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
        getServer().getMessenger().registerIncomingPluginChannel(this, "PartiesChannel", new PartiesChannelListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "PartiesChannel");
        getServer().getMessenger().registerOutgoingPluginChannel(this, MESSAGE_CHANNEL);

        initConnection();
        jedisAccess = new JedisAccess("127.0.0.1", 6379, "%]h48Ty7UBC?D+439zg%XeV6Pm#k~&9y");
        jedisAccess.initConnection();

        registerCommands();
        registerListeners();

        System.out.println("[" + getDescription().getName() + "] enable");
        if (getConfig().getBoolean("bossbar")) {
            Bukkit.getScheduler().runTaskTimer(this, new BossBarRunnable(this), 20L, 20L);
        }

        new EndoSkullPlaceholder().register();
        new CloudNetExpansion().register();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ClassementTask(this), 100, 20 * 20);

        createServerFile();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getServerName().contains("-")) {
                    if (ServerType.getByName(Bukkit.getServerName().split("-")[0]) != null) {
                        serverType = ServerType.getByName(Bukkit.getServerName().split("-")[0]);
                        jedisAccess.getServerpool().getResource().set(Bukkit.getServerName(), ServerState.ONLINE.toString());
                        System.out.println("[EndoSkull] Ajout du serveur dans le redis, prêt à recevoir des joueurs");
                    } else {
                        serverType = ServerType.UNKNOW;
                        System.out.println("[EndoSkull] Type de serveur inconnu");
                    }
                }
            }
        }.runTaskLaterAsynchronously(this, 3 * 20);


        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            for (String key : j.keys("nick/*")) {
                nicks.put(UUID.fromString(key.substring(6)), j.get(key));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            j.close();
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            JedisAccess.getUserpool().getResource().subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if (channel.equalsIgnoreCase("EndoSkullNick")) {
                        if (message.startsWith("nick:")) {
                            String[] split = message.split(":");
                            UUID uuid = UUID.fromString(split[1]);
                            String name = split[2];
                            Player player = Bukkit.getPlayer(uuid);
                            //EndoSkullAPI.nick(player, name);
                            Main.getInstance().getNicks().put(uuid, name);
                        }
                        if (message.startsWith("unnick:")) {
                            String[] split = message.split(":");
                            UUID uuid = UUID.fromString(split[1]);
                            nicks.remove(uuid);
                            Player player = Bukkit.getPlayer(uuid);
                            //EndoSkullAPI.unnick(player, true);
                        }
                    }
                }
            }, "EndoSkullNick");
        });

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            manager.addPacketListener(new TabListener(manager));
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (Player pls : Bukkit.getOnlinePlayers()) {
            pls.kickPlayer("§eEndoSkull §8>> §cLe serveur sur lequel vous étiez s'est arrêté");
        }
        JedisAccess.getServerpool().getResource().del(Bukkit.getServerName());
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
        getCommand("load").setExecutor(new LoadCommand(this));
        getCommand("motdeditor").setExecutor(new MotdCommand());
        getCommand("join").setExecutor(new JoinCommand(this));
        //getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("deploy").setExecutor(new DeployCommand(this));
        getCommand("sound").setExecutor(new SoundCommand());
        getCommand("endoskull").setExecutor(new EndoSkullCommand(this));
        getCommand("forward").setExecutor(new ForwardCommand(this));

        getCommand("endoskullapi").setExecutor(new EndoSkullApiCommand(this));
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerChat(this), this);
        pm.registerEvents(new ClickListener(this), this);
        pm.registerEvents(new PlayerInv(this), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new CustomGuiListener(), this);
        pm.registerEvents(new MotdListener(), this);
        pm.registerEvents(new VanishListener(), this);
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
        return mysql;
    }

    public HashMap<Player, Location> getWaitingSetting() {
        return waitingSetting;
    }

    public HashMap<Player, CustomGui> getOpeningKeys() {
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

    public JedisAccess getJedisAccess() {
        return jedisAccess;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public HashMap<UUID, String> getNicks() {
        return nicks;
    }
}
