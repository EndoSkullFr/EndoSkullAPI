package fr.endoskull.api;

import com.bringholm.nametagchanger.NameTagChanger;
import com.bringholm.nametagchanger.skin.Skin;
import com.bringholm.nametagchanger.skin.SkinCallBack;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import fr.endoskull.api.data.redis.JedisAccess;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import stackunderflow.skinapi.api.SkinAPI;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
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
            GameProfile profile = ((CraftPlayer) player).getHandle().getProfile();
            setSkin(player);
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

    public static void setSkin(Player player) {
        NameTagChanger.INSTANCE.getSkin("mustafayy06", (skin, successful, exception) -> {
            if (successful) {
                // Do our stuff with the skin!
                System.out.println("Wohoo! We got the skin! " + skin);
                NameTagChanger.INSTANCE.setPlayerSkin(player, skin);
            } else {
                System.out.println("Couldn't get skin :(" + exception);
            }
        });
    }

    public static void addLog(UUID uuid, String message){
        try {
            File file = new File("/root/logging/" + uuid + ".txt");
            if (!file.getParentFile().exists()) file.getParentFile().mkdir();
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
