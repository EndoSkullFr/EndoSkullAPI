package fr.endoskull.api.utils;

import java.util.Arrays;
import java.util.List;

public enum RankUnit {
    FONDATEUR("§4Admin", 10),
    RESPONSABLE_DEVELOPPEUR("§4Admin", 11),
    MODERATEUR1("§6Modo+", 12),
    MODERATEUR("§9Modérateur", 13),
    DEVELOPPEUR("§dDéveloppeur", 14),
    HELPEUR("§aHelpeur", 15),
    BUILDEUR("§2Buildeur", 16),

    INFLUENCEUR("§cInflueneur§f", 75),
    DIAMANT("§bDiamant", 76),
    OR("§eOr", 77),
    FER("§fFer", 78),
    JOUEUR("§7Joueur", 79);


    private String prefix;
    private int power;

    RankUnit(String prefix, int power) {
        this.prefix = prefix;
        this.power = power;
    }

    public static RankUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(RankUnit.JOUEUR);
    }

    public static boolean exist(String name) {
        for (RankUnit rank : values()) {
            if (rank.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static RankUnit getByPower(int power){
        return Arrays.stream(values()).filter(r -> r.getPower() == power).findAny().orElse(RankUnit.JOUEUR);
    }

    public String getName() {
        return this.toString();
    }

    public int getPower() {
        return power;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getPermissions() {
        return RankApi.getPermissions(this);
    }
}
