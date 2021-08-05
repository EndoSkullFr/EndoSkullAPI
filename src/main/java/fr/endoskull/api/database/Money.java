package fr.endoskull.api.database;

import fr.endoskull.api.Main;

import java.sql.SQLException;
import java.util.UUID;

public class Money {
    private static final String TABLE = "accounts";

    public static void setup(UUID uuid){
        if (uuid == null) return;
        Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(!rs.next()){
                    Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (uuid, money) VALUES ('" + uuid + "', '" + 0 + "')");
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

    public static double getMoney(UUID uuid){
        if (!exist(uuid)) setup(uuid);
        return (double) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    return rs.getDouble("money");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

    public static void addMoney(UUID uuid, double number){
        setMoney(uuid, getMoney(uuid) + number);
    }
    public static void removeMoney(UUID uuid, double number){
        setMoney(uuid, getMoney(uuid) - number);
    }
    public static void setMoney(UUID uuid, double number){
        if (uuid == null) return;
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET money='" +  number + "' WHERE uuid='" + uuid + "'");
    }
}
