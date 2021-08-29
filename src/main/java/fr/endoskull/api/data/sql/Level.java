package fr.endoskull.api.data.sql;

import fr.endoskull.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Level {/*
    private static final String TABLE = "levels";

    public static void setup(UUID uuid){
        if (uuid == null) return;
        Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(!rs.next()){
                    Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (uuid, level, xp) VALUES ('" + uuid + "', '" + 1 + "', '" + 0d + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
                    
        });
    }

    public static boolean exist(UUID uuid) {
        return (boolean) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public static List<UUID> getAllUuids() {
        return (List<UUID>) Main.getInstance().getMySQL().query("SELECT `uuid` FROM `" + TABLE + "` WHERE 1", rs -> {
            try {
                List<UUID> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(UUID.fromString(rs.getString("uuid")));
                }
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public static double getXp(UUID uuid){
        if (!exist(uuid)) setup(uuid);
        return (double) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    return rs.getDouble("xp");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0d;
        });
    }

    public static void addXp(UUID uuid, double number){
        setXp(uuid, getXp(uuid) + number);
    }
    public static void removeXp(UUID uuid, double number){
        setXp(uuid, getXp(uuid) - number);
    }
    public static void setXp(UUID uuid, double number){
        if (uuid == null) return;
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET xp='" +  number + "' WHERE uuid='" + uuid + "'");
        checkXp(uuid);
        checkNegatifXp(uuid);
    }

    public static int getLevel(UUID uuid){
        if (!exist(uuid)) setup(uuid);
        return (int) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    return rs.getInt("level");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public static void addLevel(UUID uuid, int number){
        setLevel(uuid, getLevel(uuid) + number);
    }
    public static void removeLevel(UUID uuid, int number){
        setLevel(uuid, getLevel(uuid) - number);
    }
    public static void setLevel(UUID uuid, int number){
        if (uuid == null) return;
        if (number <= 0) return;
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET level='" +  number + "' WHERE uuid='" + uuid + "'");
    }

    public static Double xpToLevelSup(UUID uuid) {
        return 20d + (getLevel(uuid) * 10d);
    }

    public static void checkXp(UUID uuid) {
        if (getXp(uuid) >= xpToLevelSup(uuid)) {
            removeXp(uuid, xpToLevelSup(uuid));
            addLevel(uuid, 1);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.5F);
            }
            checkXp(uuid);
        }
    }
    public static void checkNegatifXp(UUID uuid) {
        if (getXp(uuid) < 0) {
            setXp(uuid, getXp(uuid) + (xpToLevelSup(uuid) - 10));
            removeLevel(uuid, 1);
            checkNegatifXp(uuid);
        }
    }*/
}
