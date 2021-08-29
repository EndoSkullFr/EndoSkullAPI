package fr.endoskull.api.data.sql;

import fr.endoskull.api.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.SQLException;

public class BoxLocation {/*
    private static final String TABLE = "location";

    public static void setup(String name){
        Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE name='" + name + "'", rs -> {
            try {
                if(!rs.next()){
                    Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (name, location) VALUES ('" + name + "', '" + getStringLoc(Bukkit.getWorlds().get(0).getSpawnLocation()) + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
                    
        });
    }

    public static Location getLoc(String name){
        return (Location) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE name='" + name + "'", rs -> {
            try {
                if(rs.next()){
                    String[] arg = rs.getString("location").split(",");
                    double[] parsed = new double[3];
                    for (int a = 0; a < 3; a++) {
                        parsed[a] = Double.parseDouble(arg[a+1]);
                    }

                    return new Location (Bukkit.getWorld(arg[0]), parsed[0], parsed[1], parsed[2]);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        });
    }

    public static void setLocation(String name, Location loc){
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET location='" + getStringLoc(loc) + "' WHERE name='" + name + "'");

    }

    public static String getStringLoc(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }*/
}
