package fr.endoskull.api.database;

import fr.endoskull.api.Main;
import fr.endoskull.api.utils.RankUnit;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Account {
    private static final String TABLE = "ranks";

    public static void setup(UUID uuid){
        Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(!rs.next()){
                    Main.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (uuid, grades) VALUES ('" + uuid + "', '" + Arrays.asList(RankUnit.JOUEUR) + "')");
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

    public static List<RankUnit> getRankList(UUID uuid){
        if (!exist(uuid)) setup(uuid);
        return (List<RankUnit>) Main.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    List<RankUnit> result = new ArrayList<>();
                    for (String rankName : rs.getString("grades").replace("[", "").replace("]", "").replace(" ", "").split(",")) {
                        result.add(RankUnit.getByName(rankName));
                    }
                    return result;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Arrays.asList(RankUnit.JOUEUR);
        });
    }

    public static void addRank(RankUnit rank, UUID uuid){
        List<RankUnit> rankList = getRankList(uuid);
        if (!rankList.contains(rank)) rankList.add(rank);
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET grades='" + rankList + "' WHERE uuid='" + uuid + "'");
        if (Bukkit.getPlayer(uuid) != null) {
            Main.getInstance().getRank().changeRank(Bukkit.getPlayer(uuid));
        }
    }
    public static void removeRank(RankUnit rank, UUID uuid){
        List<RankUnit> rankList = getRankList(uuid);
        if (rankList.contains(rank)) rankList.remove(rank);
        Main.getInstance().getMySQL().update("UPDATE " + TABLE + " SET grades='" + rankList + "' WHERE uuid='" + uuid + "'");
        if (Bukkit.getPlayer(uuid) != null) {
            Main.getInstance().getRank().changeRank(Bukkit.getPlayer(uuid));
        }
    }
}
