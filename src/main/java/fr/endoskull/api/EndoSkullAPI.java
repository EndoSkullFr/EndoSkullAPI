package fr.endoskull.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.endoskull.api.data.redis.JedisAccess;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import stackunderflow.skinapi.api.SkinAPI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

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
            //setSkin(player);
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
            TabAPI tabAPI = TabAPI.getInstance();
            TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
            tabPlayer.setTemporaryGroup(user.getPrimaryGroup());
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

    private static void setSkin(Player player) {
        SkinAPI skinAPI = new SkinAPI();
        skinAPI.changePlayerSkin(player, "MHF_GitHub");

    }

    public static void addLog(UUID uuid, String message){
        try {
            File file = new File("/root/logging/" + uuid + ".txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
