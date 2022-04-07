package fr.endoskull.api.bungee.utils;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.api.ProxyServer;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class BungeePlayerInfos {

    public static UUID getUuidFromName(String name) {
        if (ProxyServer.getInstance().getPlayer(name) != null) {
            return ProxyServer.getInstance().getPlayer(name).getUniqueId();
        }
        if (name.equalsIgnoreCase("none")) return null;
        AtomicReference<UUID> uuid = new AtomicReference<>(null);
        BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + JedisManager.TABLE + " WHERE name='" + name + "'", rs -> {
            try {
                if(rs.next()){
                    uuid.set(UUID.fromString(rs.getString("uuid")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });

        return uuid.get();
    }

    public static String getNameFromUuid(UUID uuid) {
        if (ProxyServer.getInstance().getPlayer(uuid) != null) {
            return ProxyServer.getInstance().getPlayer(uuid).getName();
        }
        AtomicReference<String> name = new AtomicReference<>(null);
        BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + JedisManager.TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    name.set(rs.getString("name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });

        return name.get();
    }

}