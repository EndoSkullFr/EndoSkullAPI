package fr.endoskull.api.spigot.listeners;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.server.ServerState;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisAccess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerJoin implements Listener {
    private Main main;
    public PlayerJoin(Main main) {
        this.main = main;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoinLang(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Account account = AccountProvider.getAccount(player.getUniqueId());
        Main.getLangs().put(player, account.getLang());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        /*Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("UUID");
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //UUID uuid = PlayerInfos.getUuidFromName(player.getName());
            //if (!main.getUuidsByName().containsKey(player)) main.getUuidsByName().put(player.getName(), uuid);
        });*/
        EndoSkullAPI.loadPrefix(player.getUniqueId());
        if (!main.getServerType().isMultiArena() && main.getServerType() != ServerType.UNKNOW) {
            Jedis j = null;
            try {
                j = JedisAccess.getServerpool().getResource();
                j.set("online/" + Bukkit.getServerName(), String.valueOf(Bukkit.getOnlinePlayers().size()));
            } finally {
                j.close();
            }
        }
        if (Bukkit.getOnlinePlayers().size() >= main.getServerType().getSemiFull()) {
            Jedis j = null;
            try {
                j = JedisAccess.getServerpool().getResource();
                j.set(Bukkit.getServerName(), ServerState.SEMI_FULL.toString());
            } finally {
                j.close();
            }
        }
        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
            Jedis j = null;
            try {
                j = JedisAccess.getServerpool().getResource();
                j.set(Bukkit.getServerName(), ServerState.FULL.toString());
            } finally {
                j.close();
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuitLang(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Main.getLangs().remove(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (!main.getServerType().isMultiArena() && main.getServerType() != ServerType.UNKNOW) {
            Jedis j = null;
            try {
                j = JedisAccess.getServerpool().getResource();
                List<? extends Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                players.remove(player);
                j.set("online/" + Bukkit.getServerName(), String.valueOf(players.size()));
            } finally {
                j.close();
            }
        }
    }

    @EventHandler
    public void onLoginStaff(PlayerLoginEvent event){
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL))
            if (event.getPlayer().hasPermission("endoskull.staff"))
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
    }
}
