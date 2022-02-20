package fr.endoskull.api;

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
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import stackunderflow.skinapi.api.SkinAPI;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.*;

public class EndoSkullAPI {

    public static void nick(Player player, String nick) {
        if (player != null) {
            player.setDisplayName(nick);
            player.setCustomName(nick);
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            user.data().add(Node.builder("prefix.200.&7").build());
            luckPerms.getUserManager().saveUser(user);
            try {
                TabAPI tabAPI = TabAPI.getInstance();
                TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
                tabPlayer.setTemporaryGroup("default");
            } catch (Exception e) {
            }
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
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> setSkin(player, true));
        }
    }

    public static void unnick(Player player, boolean changeSkin) {
        if (player != null) {
            player.setDisplayName(player.getName());
            player.setCustomName(player.getName());
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            user.data().remove(Node.builder("prefix.200.&7").build());
            luckPerms.getUserManager().saveUser(user);
            try {
                TabAPI tabAPI = TabAPI.getInstance();
                TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
                tabPlayer.setTemporaryGroup(user.getPrimaryGroup());
            } catch (Exception e) {
            }
            Jedis j = null;
            try {
                j = JedisAccess.getUserpool().getResource();
                j.del("nick/" + player.getUniqueId());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                j.close();
            }
            if (changeSkin) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> setSkin(player, false));
            }
        }
    }

    public static void setSkin(Player player, boolean b) {
        GameProfile profile = ((CraftPlayer) player).getHandle().getProfile();
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                ((CraftPlayer) player).getHandle()));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            PlayerConnection c = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;

            c.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                    ((CraftPlayer) player).getHandle()));
        }

        profile.getProperties().removeAll("textures");
        if (b) {
            profile.getProperties().put("textures", getSkin());
        } else {
            profile.getProperties().put("textures", getDefaultSkin(player));
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            connection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                    ((CraftPlayer) player).getHandle()));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.equals(player)) continue;
                PlayerConnection c = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;

                c.sendPacket(new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                        ((CraftPlayer) player).getHandle()));
            }
        }, 5);
    }

    private static Property getSkin() {
        Random r = new Random();
        String url1 = "https://api.mineskin.org/get/list/" + r.nextInt(100);
        try {
            @SuppressWarnings("deprecation")
            String json1 = IOUtils.toString(new URL(url1));
            if(json1.isEmpty()) return null;
            JSONObject jsonObject1 = (JSONObject) JSONValue.parseWithException(json1);

            JSONArray skins = (JSONArray) jsonObject1.get("skins");
            Iterator<JSONObject> iterator = skins.iterator();
            List<String> skinList = new ArrayList<>();
            while (iterator.hasNext()) {
                JSONObject skinObject = iterator.next();
                skinList.add(skinObject.get("uuid").toString());
            }
            String skinUuid = skinList.get(r.nextInt(skinList.size()));
            String url2 = "https://api.mineskin.org/get/uuid/" + skinUuid;
            String json2 = IOUtils.toString(new URL(url2));
            if(json2.isEmpty()) return null;
            JSONObject jsonObject2 = (JSONObject) JSONValue.parseWithException(json2);
            JSONObject skinData = (JSONObject) jsonObject2.get("data");
            JSONObject texture = (JSONObject) skinData.get("texture");
            return new Property("textures", texture.get("value").toString(), texture.get("signature").toString());

        } catch (IOException | ParseException e) {
        }
        return null;
    }

    private static Property getDefaultSkin(Player player) {
        Random r = new Random();
        String url1 = "https://sessionserver.mojang.com/session/minecraft/profile/" + player.getUniqueId() + "?unsigned=false";
        try {
            @SuppressWarnings("deprecation")
            String json1 = IOUtils.toString(new URL(url1));
            if(json1.isEmpty()) return null;
            JSONObject jsonObject1 = (JSONObject) JSONValue.parseWithException(json1);

            JSONArray skins = (JSONArray) jsonObject1.get("properties");
            Iterator<JSONObject> iterator = skins.iterator();
            while (iterator.hasNext()) {
                JSONObject texture = iterator.next();
                if (texture.get("name").toString().equalsIgnoreCase("textures")) {
                    return new Property("textures", texture.get("value").toString(), texture.get("signature").toString());
                }
            }

        } catch (IOException | ParseException e) {
        }
        return null;
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
