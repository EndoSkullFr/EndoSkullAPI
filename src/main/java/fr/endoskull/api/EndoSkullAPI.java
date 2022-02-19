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
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
            setSkin(player, "95977a99841e021713fac48b8bad2b2dd243e91cd3c37945d9d6f213ab4c2265");
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

    private static void setSkin(Player player, String texture) {
        player.sendMessage("blabla");
        CraftPlayer cp = (CraftPlayer) player;
        GameProfile gp = cp.getProfile();

        Iterator<Property> b = gp.getProperties().get("textures").iterator();
        String text = null;
        while(b.hasNext()) {
            Property c = b.next();
            if(c.getName().equalsIgnoreCase("textures")) {
                text = Base64Coder.decodeString(c.getValue());
                //System.out.println(text);
                text.replaceAll("/\"SKIN\"[.*?]\"}}}/", "\"SKIN\":{\"url\":\""+texture+"\"}}}");
                Base64Coder.encodeString(text);
                //System.out.println(text);
            }
        }
        gp.getProperties().put("textures", new Property("textures", text));

        //this 2 shedular are for make the change visible to the other players
        List<Player> toShow = new ArrayList<>();
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

            @Override
            public void run() {
                for(Player o : Bukkit.getOnlinePlayers()) {
                    if (o.canSee(player)) {
                        o.hidePlayer(player);
                        toShow.add(o);
                    }
                }

            }
        }, 0);

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {

            @Override
            public void run() {
                for(Player o : toShow) {
                    o.showPlayer(player);
                }

            }
        }, 15);
    }
}
