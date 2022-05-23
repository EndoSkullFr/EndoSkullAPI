package fr.endoskull.api.commons.paf;

import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.FriendRequest;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.data.sql.MySQL;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.*;

public class FriendUtils {

    private static HashMap<UUID, List<FriendRequest>> requests = new HashMap<>();

    public static void addRequest(UUID sender, UUID receiver) {
        MySQL.getInstance().update("INSERT INTO `friend_requests`(`sender`, `receiver`, `expiry`) VALUES ('" + sender + "','" + receiver + "', '" + (System.currentTimeMillis() + (1000*60*5)) + "')");
        if (PAFTask.getFriendRequests().containsKey(sender)) {
            List<UUID> uuids = PAFTask.getFriendRequests().get(sender);
            uuids.add(receiver);
            PAFTask.getFriendRequests().put(sender, uuids);
        } else {
            PAFTask.getFriendRequests().put(sender, new ArrayList<>(Arrays.asList(receiver)));
        }
    }

    public static boolean hasRequestFrom(UUID uuid, UUID sender) {
        return (boolean) MySQL.getInstance().query("SELECT * FROM `friend_requests` WHERE sender='" + sender + "' AND receiver='" + uuid + "'", rs -> {
            try {
                if (rs.next()){
                    boolean avaible = System.currentTimeMillis() < rs.getLong("expiry");
                    if (!avaible) removeRequest(sender, uuid);
                    return avaible;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
        /*if (!requests.containsKey(sender)) return false;
        for (FriendRequest request : requests.get(sender)) {
            if (request.getReceiver().equals(uuid)) return true;
        }
        return false;*/
    }

    public static void removeRequest(UUID sender, UUID receiver) {
        MySQL.getInstance().update("DELETE FROM `friend_requests` WHERE sender='" + sender + "' AND receiver='" + receiver + "'");
    }

    public static List<UUID> getRequests(UUID uuid) {
        List<UUID> requests = new ArrayList<>();
        MySQL.getInstance().query("SELECT * FROM `friend_requests` WHERE receiver='" + uuid + "'", rs -> {
            try {
                while (rs.next()) {
                    if (System.currentTimeMillis() < rs.getLong("expiry")) {
                        requests.add(UUID.fromString(rs.getString("sender")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return requests;
    }

    public static List<UUID> getFriends(UUID uuid) {
        List<UUID> friends = new ArrayList<>();
        MySQL.getInstance().query("SELECT * FROM friends WHERE uuid1='" + uuid + "' OR uuid2='" + uuid + "'", rs -> {
            try {
                while (rs.next()){
                    if (rs.getString("uuid1").equalsIgnoreCase(uuid.toString())) {
                        friends.add(UUID.fromString(rs.getString("uuid2")));
                    } else {
                        friends.add(UUID.fromString(rs.getString("uuid1")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return friends;
    }

    public static List<UUID> getOrderedFriends(UUID uuid) {
        List<UUID> friends = getFriends(uuid);
        List<UUID> orderedFriends = new ArrayList<>();
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            for (UUID friend : friends) {
                if (j.exists("account/" + friend)) {
                    orderedFriends.add(friend);
                    System.out.println(friend);
                }
            }
        } finally {
            j.close();
        }
        for (UUID friend : friends) {
            if (!orderedFriends.contains(friend)) {
                orderedFriends.add(friend);
                System.out.println(friend);
            }
        }
        System.out.println(orderedFriends);
        return orderedFriends;
    }

    public static boolean isOnline(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.exists("account/" + uuid);
        } finally {
            j.close();
        }
    }

    public static void addFriend(UUID uuid1, UUID uuid2) {
        MySQL.getInstance().update("INSERT INTO `friends`(`uuid1`, `uuid2`) VALUES ('" + uuid1 + "','" + uuid2 + "')");
    }

    public static void removeFriend(UUID uuid1, UUID uuid2) {
        MySQL.getInstance().update("DELETE FROM `friends` WHERE (`uuid1`='" + uuid1 + "' AND `uuid2`='" + uuid2 + "') OR (`uuid1`='" + uuid2 + "' AND `uuid2`='" + uuid1 + "')");
    }

    public static boolean areFriends(UUID uuid1, UUID uuid2) {
        return (boolean) MySQL.getInstance().query("SELECT * FROM `friends` WHERE (`uuid1`='" + uuid1 + "' AND `uuid2`='" + uuid2 + "') OR (`uuid1`='" + uuid2 + "' AND `uuid2`='" + uuid1 + "')" ,rs -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static void setSetting(UUID uuid, FriendSettingsSpigot setting, String value) {
        MySQL.getInstance().query("SELECT * FROM `friend_settings` WHERE uuid='" + uuid + "' AND setting='" + setting + "'", rs -> {
            try {
                if (rs.next()) {
                    MySQL.getInstance().update("UPDATE `friend_settings` SET " + "value" + "='" + value + "' WHERE uuid='" + uuid + "' AND setting='" + setting + "'");
                } else {
                    MySQL.getInstance().update("INSERT INTO `friend_settings` (uuid, setting, value) VALUES ('" + uuid + "', '" + setting + "', '" + value + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static String getSetting(UUID uuid, FriendSettingsBungee setting) {
        return (String) MySQL.getInstance().query("SELECT * FROM `friend_settings` WHERE uuid='" + uuid + "' AND setting='" + setting + "'", rs -> {
            try {
                if (rs.next()) {
                    return rs.getString("value");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "1";
        });
    }

    public static String getSetting(UUID uuid, FriendSettingsSpigot setting) {
        return getSetting(uuid, FriendSettingsBungee.valueOf(setting.name()));
    }

    public static HashMap<UUID, List<FriendRequest>> getRequests() {
        return requests;
    }
}
