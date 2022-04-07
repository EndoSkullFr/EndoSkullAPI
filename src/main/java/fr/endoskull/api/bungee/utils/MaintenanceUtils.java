package fr.endoskull.api.bungee.utils;

import fr.endoskull.api.data.sql.MySQL;

import java.sql.SQLException;
import java.util.UUID;

public class MaintenanceUtils {

    public static void setMaintenance(String service, boolean value) {
        MySQL.getInstance().query("SELECT * FROM maintenance WHERE `service`='" + service + "'", rs -> {
            try {
                if (rs.next()) {
                    MySQL.getInstance().update("UPDATE maintenance SET `service`='" + service + "',`value`='" + (value ? 1 : 0) + "' WHERE `service`='" + service + "'");
                } else {
                    MySQL.getInstance().update("INSERT INTO maintenance(`service`, `value`) VALUES ('" + service + "','" + (value ? 1 : 0) + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean isInMaintenance(String service) {
        return (boolean) MySQL.getInstance().query("SELECT * FROM maintenance WHERE `service`='" + service + "'", rs -> {
            try {
                if (rs.next()) {
                    return rs.getBoolean("value");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static void whitelist(String name) {
        MySQL.getInstance().query("SELECT * FROM whitelist WHERE `name`='" + name + "'", rs -> {
            try {
                if (!rs.next()) {
                    MySQL.getInstance().update("INSERT INTO `whitelist`(`name`) VALUES ('" + name + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void unwhitelist(String name) {
        MySQL.getInstance().update("DELETE FROM `whitelist` WHERE `name`='" + name + "'");
    }



    public static boolean isWhitelisted(String name) {
        return (boolean) MySQL.getInstance().query("SELECT * FROM whitelist WHERE `name`='" + name + "'", rs -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

}
