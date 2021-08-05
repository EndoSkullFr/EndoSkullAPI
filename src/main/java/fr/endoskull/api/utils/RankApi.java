package fr.endoskull.api.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.database.Account;
import fr.endoskull.api.database.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class RankApi {

    public static List<RankUnit> getRankList(UUID uuid) {
        return Account.getRankList(uuid);
    }

    public static RankUnit getRank(UUID uuid) {
        RankUnit rank = RankUnit.JOUEUR;
        for (RankUnit r : getRankList(uuid)) {
            if (r.getPower() < rank.getPower()) {
                rank = r;
            }
        }
        return rank;
    }

    public static boolean hasRank(UUID uuid, RankUnit rankUnit) {
        return getRankList(uuid).contains(rankUnit);
    }

    public static void addRank(UUID uuid, RankUnit rankUnit) {
        Account.addRank(rankUnit, uuid);
    }

    public static void removeRank(UUID uuid, RankUnit rankUnit) {
        Account.removeRank(rankUnit, uuid);
    }

    public static List<String> getPermissions(RankUnit rank) {
        return Permissions.getPermList(rank);
    }

    public static boolean hasPerm(RankUnit rank, String permission) {
        return getPermissions(rank).contains(permission);
    }

    public static void addPermission(RankUnit rank, String permission) {
        if (!hasPerm(rank, permission)) {
            Permissions.addPermission(permission, rank);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RankApi.hasRank(Main.getInstance().getUuidsByName().get(player.getName()), rank)) {
                Main.getInstance().getRank().reloadPlayer(player);
            }
        }
    }

    public static void removePermission(RankUnit rank, String permission) {
        if (hasPerm(rank, permission)) {
            Permissions.removePermission(permission, rank);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RankApi.hasRank(Main.getInstance().getUuidsByName().get(player.getName()), rank)) {
                Main.getInstance().getRank().reloadPlayer(player);
            }
        }
    }

}
