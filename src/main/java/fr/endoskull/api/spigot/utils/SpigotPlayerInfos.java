package fr.endoskull.api.spigot.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.JedisManager;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SpigotPlayerInfos {

    public static UUID getUuidFromName(String name) {
        if (Bukkit.getPlayer(name) != null) {
            return Bukkit.getPlayer(name).getUniqueId();
        }
        if (name.equalsIgnoreCase("none")) return null;
        AtomicReference<UUID> uuid = new AtomicReference<>(null);
        Main.getInstance().getMySQL().query("SELECT * FROM " + JedisManager.TABLE + " WHERE name='" + name + "'", rs -> {
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
        if (Bukkit.getPlayer(uuid) != null) {
            return Bukkit.getPlayer(uuid).getName();
        }
        AtomicReference<String> name = new AtomicReference<>(null);
        Main.getInstance().getMySQL().query("SELECT * FROM " + JedisManager.TABLE + " WHERE uuid='" + uuid + "'", rs -> {
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