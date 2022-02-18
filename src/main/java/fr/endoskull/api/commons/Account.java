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

    public int getVoteKey() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Integer.parseInt(j.hget("account/" + uuid, "voteKey"));
        } finally {
            j.close();
        }
    }

    public Account setVoteKey(int voteKey) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "voteKey", String.valueOf(voteKey));
        } finally {
            j.close();
        }
        return this;
    }

    public int getUltimeKey() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Integer.parseInt(j.hget("account/" + uuid, "ultimeKey"));
        } finally {
            j.close();
        }
    }

    public Account setUltimeKey(int ultimeKey) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "ultimeKey", String.valueOf(ultimeKey));
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

    public double getBooster() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return Double.parseDouble(j.hget("account/" + uuid, "booster"));
        } finally {
            j.close();
        }
    }

    public BoosterManager getBoost() {
        return new BoosterManager(BoosterManager.getBooster(uuid));
    }

    public Account setBooster(double booster) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "booster", String.valueOf(booster));
        } finally {
            j.close();
        }
        return this;
    }

    public String getStringBooster() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(getRealBooster());
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

    public void sendToRedis() {
        /*AccountProvider accountProvider = new AccountProvider(UUID.fromString(uuid));
        accountProvider.sendAccountToRedis(this);*/
    }

    private List<String> stringToArray(String kitsString) {
        if (kitsString.length() == 0) return new ArrayList<>();
        if (kitsString.contains(",")) {
            return Arrays.asList(kitsString.split(","));
        } else {
            return Arrays.asList(kitsString);
        }
    }

    public String getSelectedKit() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.hget("account/" + uuid, "selectedKit");
        } finally {
            j.close();
        }
    }

    public Account setSelectedKit(String selectedKit) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "selectedKit", selectedKit);
        } finally {
            j.close();
        }
        return this;
    }

    public String getEffectsString() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.hget("account/" + uuid, "effects");
        } finally {
            j.close();
        }
    }

    public String getSelectedEffect() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.hget("account/" + uuid, "selectedEffect");
        } finally {
            j.close();
        }
    }

    public Account setSelectedEffect(String selectedEffect) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "selectedEffect", selectedEffect);
        } finally {
            j.close();
        }
        return this;
    }

    public List<String> getEffects() {
        if (getEffectsString().contains(",")) {
            return Arrays.asList(getEffectsString().split(","));
        } else {
            return Arrays.asList(getEffectsString());
        }
    }

    public Account setEffects(List<String> effects) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "effects", effects.toString().replace(", ", ",").replace("[", "").replace("]", ""));
        } finally {
            j.close();
        }
        return this;
    }

    public Account setEffects(String effects) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "effects", effects);
        } finally {
            j.close();
        }
        return this;
    }

    public Account addEffect(String effect) {
        List<String> effects = new ArrayList<>(getEffects());
        effects.add(effect);
        setEffects(effects);
        return this;
    }

    public Account removeEffect(String effect) {
        List<String> effects = new ArrayList<>(getEffects());
        effects.remove(effect);
        setEffects(effects);
        return this;
    }

    public double getRealBooster() {
        double booster = getBooster();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            if (player.hasPermission("group.hero")) {
                booster += 1;

            }
            else if (player.hasPermission("group.vip")) {
                booster += 0.5;
            }
        }
        return booster;
    }

    public int getCoinsKey() {
        return 0;
    }

    public int getKitKey() {
        return 0;
    }

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

    public String getDiscord() {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.hget("account/" + uuid, "discord");
        } finally {
            j.close();
        }
    }

    public Account setDiscord(String discord) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("account/" + uuid, "discord", discord);
        } finally {
            j.close();
        }
        return this;
    }
}
