package fr.endoskull.api.commons;

import fr.bebedlastreat.cache.CacheAPI;
import fr.endoskull.api.data.redis.JedisAccess;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Account implements Cloneable {
    private UUID uuid;

    public Account() {}

    public Account(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.hget("account/" + uuid, "name");
        } finally {
            j.close();
        }
    }

    public Account setName(String name) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "name", name);
        } finally {
            j.close();
        }
        return this;
    }

    public int getLevel() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Integer.parseInt(j.hget("account/" + uuid, "level"));
        } finally {
            j.close();
        }
    }

    public Account setLevel(int level) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "level", String.valueOf(level));
        } finally {
            j.close();
        }
        return this;
    }

    public Account addLevel(int level) {
        setLevel(getLevel() + level);
        return this;
    }

    public Account removeLevel(int level) {
        setLevel(getLevel() - level);
        return this;
    }

    public double getXp() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Double.parseDouble(j.hget("account/" + uuid, "xp"));
        } finally {
            j.close();
        }
    }

    public Account addXp(Double xp) {
        setXp(getXp() + xp);
        return this;
    }

    public Account removeXp(Double xp) {
        setXp(getXp() - xp);
        return this;
    }

    public Account setXp(double xp) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "xp", String.valueOf(xp));
        } finally {
            j.close();
        }
        checkLevel();
        return this;
    }

    private void checkLevel() {
        if (getXp() >= xpToLevelSup()) {
            setXp(getXp() - xpToLevelSup());
            setLevel(getLevel() + 1);
            checkLevel();
        }
    }

    public BoosterManager getBoost() {
        return new BoosterManager(BoosterManager.getBooster(uuid));
    }

    public double getSolde() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Double.parseDouble(j.hget("account/" + uuid, "solde"));
        } finally {
            j.close();
        }
    }

    public Account addMoney(double value) {
        setSolde(getSolde() + value);
        return this;
    }

    public Account addMoneyWithBooster(double value) {
        addMoney(value * getBoost().getRealBooster());
        return this;
    }

    public Account removeMoney(double value) {
        setSolde(getSolde() - value);
        return this;
    }

    public String getStringSolde() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        if (getSolde() > 1000000) {
            return df.format(getSolde()/1000000) + "M";
        }
        if (getSolde() > 1000) {
            return df.format(getSolde()/1000) + "k";
        }
        return df.format(getSolde());
    }

    public Account setSolde(double solde) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "solde", String.valueOf(solde));
        } finally {
            j.close();
        }
        return this;
    }

    public Account setFirstJoin(long firstJoin) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "first_join", String.valueOf(firstJoin));
        } finally {
            j.close();
        }
        return this;
    }



    public long getFirstJoin() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Long.parseLong(j.hget("account/" + uuid, "first_join"));
        } finally {
            j.close();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Account clone() {
        try {
            return (Account) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double xpToLevelSup() {
        return 20d + (getLevel() * 10d);
    }

    public double getLevelWithXp() {
        double level = getLevel();
        double xp = Double.valueOf(getXp() / xpToLevelSup());
        return level + xp;
    }

    public String getStringLevel() {
        /*DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(getLevelWithXp());*/
        return getLevel() + " (" + Math.round(getXp() / xpToLevelSup() * 100) + "%)";
    }

    //public void sendToRedis() {
        /*AccountProvider accountProvider = new AccountProvider(UUID.fromString(uuid));
        accountProvider.sendAccountToRedis(this);*/
    //}

    public Account setStatistic(String statistic, int value) {
        CacheAPI.set("stats/" + uuid + "/" + statistic, value);
        return this;
    }

    public int getStatistic(String statistic) {
        if (CacheAPI.keyExist("stats/" + uuid + "/" + statistic)) {
            return CacheAPI.getInt("stats/" + uuid + "/" + statistic);
        } else {
            return 0;
        }
    }

    public Account incrementStatistic(String statistic, int value) {
        setStatistic(statistic, getStatistic(statistic) + value);
        return this;
    }

    public Account incrementStatistic(String statistic) {
        setStatistic(statistic, getStatistic(statistic) + 1);
        return this;
    }

    public Account setProperty(String property, String value) {
        CacheAPI.set("properties/" + uuid + "/" + property, value);
        return this;
    }

    public String getProperty(String property) {
        if (CacheAPI.keyExist("properties/" + uuid + "/" + property)) {
            return CacheAPI.get("properties/" + uuid + "/" + property);
        } else {
            return "";
        }
    }

    public String getProperty(String property, String defaultValue) {
        if (CacheAPI.keyExist("properties/" + uuid + "/" + property)) {
            return CacheAPI.get("properties/" + uuid + "/" + property);
        } else {
            return defaultValue;
        }
    }
}
