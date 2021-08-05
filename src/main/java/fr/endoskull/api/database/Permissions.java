package fr.endoskull.api.database;

import fr.endoskull.api.Main;
import fr.endoskull.api.utils.RankUnit;
import fr.endoskull.api.utils.RankUnit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Permissions {
    private static final String TABLE = "permissions";

    public static void setup(){
        for (RankUnit rank : RankUnit.values()) {
            Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE grade='" + rank.getName() + "'", rs -> {
                try {
                    if(!rs.next()){
                        Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (grade, permissions) VALUES ('" + rank.getName() + "', '" + new ArrayList<>() + "')");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });
        }
    }

    public static List<String> getPermList(RankUnit rank){
        return (List<String>) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE grade='" + rank.getName() + "'", rs -> {
            try {
                if(rs.next()){
                    List<String> result = new ArrayList<>();
                    if (rs.getString("permissions").equals("[]")) return new ArrayList<>();
                    for (String perm : rs.getString("permissions").replace("[", "").replace("]", "").replace(" ", "").split(",")) {
                        result.add(perm);
                    }
                    return result;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        });
    }

    public static void addPermission(String permission, RankUnit rank){
        List<String> permList = getPermList(rank);
        if (!permList.contains(permission)) permList.add(permission);
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET permissions='" + permList + "' WHERE grade='" + rank.getName() + "'");
    }
    public static void removePermission(String permission, RankUnit rank){
        List<String> permList = getPermList(rank);
        if (permList.contains(permission)) permList.remove(permission);
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET permissions='" + permList + "' WHERE grade='" + rank.getName() + "'");
    }
}
