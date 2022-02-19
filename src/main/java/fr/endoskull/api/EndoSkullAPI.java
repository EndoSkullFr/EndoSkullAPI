package fr.endoskull.api;

import fr.endoskull.api.data.redis.JedisAccess;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.Socket;

public class EndoSkullAPI {

    public static void nick(Player player, String nick) {
        if (player != null) {
            player.setDisplayName(nick);
            player.setCustomName(nick);
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            user.data().add(Node.builder("prefix.200.&7").build());
            luckPerms.getUserManager().saveUser(user);
            TabAPI tabAPI = TabAPI.getInstance();
            TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
            tabPlayer.setTemporaryGroup("default");
            Jedis j = null;
            try {
                 j = JedisAccess.getUserpool().getResource();
                 j.set("nick/" + player.getUniqueId(), nick);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                j.close();
            }
        }
    }

    public static void unnick(Player player) {
        if (player != null) {
            player.setDisplayName(player.getName());
            player.setCustomName(player.getName());
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            user.data().remove(Node.builder("prefix.200.&7").build());
            luckPerms.getUserManager().saveUser(user);
            Jedis j = null;
            try {
                j = JedisAccess.getUserpool().getResource();
                j.del("nick/" + player.getUniqueId());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                j.close();
            }
        }
    }
}
