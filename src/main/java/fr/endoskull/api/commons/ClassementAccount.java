package fr.endoskull.api.commons;

import fr.endoskull.api.data.redis.JedisAccess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClassementAccount implements Cloneable {
    private UUID uuid;
    private String name;
    private int level;
    private double xp;

    public ClassementAccount() {}

    public ClassementAccount(UUID uuid, String name, int level, double xp) {
        this.uuid = uuid;
        this.name = name;
        this.level = level;
        this.xp = xp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public String getStringLevel() {
        /*DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(getLevelWithXp());*/
        return getLevel() + " (" + Math.round(getXp() / xpToLevelSup() * 100) + "%)";
    }

    public double xpToLevelSup() {
        return 20d + (getLevel() * 10d);
    }
}
