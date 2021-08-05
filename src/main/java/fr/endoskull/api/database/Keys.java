package fr.endoskull.api.database;

import fr.endoskull.api.Main;

import java.sql.SQLException;
import java.util.UUID;

public class Keys {
    private static final String TABLE = "player_key";

    public static void setup(UUID uuid){
        if (uuid == null) return;
        Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(!rs.next()){
                    Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (uuid, ultime, vote) VALUES ('" + uuid + "', '" + 0 + "', '" + 0 + "')");
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

    public static int getKeys(UUID uuid, String name){
        if (!exist(uuid)) setup(uuid);
        return (int) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    return rs.getInt(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public static void addKey(UUID uuid, String name, int number){
        setKey(uuid, name, getKeys(uuid, name) + number);
    }
    public static void removeKey(UUID uuid, String name, int number){
        setKey(uuid, name, getKeys(uuid, name) - number);
    }
    public static void setKey(UUID uuid, String name, int number){
        if (uuid == null) return;
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET " + name + "='" +  number + "' WHERE uuid='" + uuid + "'");
    }
}
