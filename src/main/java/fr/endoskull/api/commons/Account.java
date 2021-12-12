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

public class Account implements Cloneable {
    private String uuid;
    private String name;

    public Account() {}

    public Account(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;

        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "name", name);
        } finally {
            j.close();
        }
        return this;
    }

    public int getVoteKey() {
        Jedis j = null;
        int var;
        try {
            j = JedisAccess.get().getResource();
            var = Integer.parseInt(j.hget(AccountProvider.REDIS_KEY + uuid, "voteKey"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setVoteKey(int voteKey) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "voteKey", String.valueOf(voteKey));
        } finally {
            j.close();
        }
        return this;
    }

    public int getUltimeKey() {
        Jedis j = null;
        int var;
        try {
            j = JedisAccess.get().getResource();
            var = Integer.parseInt(j.hget(AccountProvider.REDIS_KEY + uuid, "ultimeKey"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setUltimeKey(int ultimeKey) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "ultimeKey", String.valueOf(ultimeKey));
        } finally {
            j.close();
        }
        return this;
    }

    public int getCoinsKey() {
        Jedis j = null;
        int var;
        try {
            j = JedisAccess.get().getResource();
            var = Integer.parseInt(j.hget(AccountProvider.REDIS_KEY + uuid, "coinsKey"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setCoinsKey(int coinsKey) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "coinsKey", String.valueOf(coinsKey));
        } finally {
            j.close();
        }
        return this;
    }

    public int getKitKey() {
        Jedis j = null;
        int var;
        try {
            j = JedisAccess.get().getResource();
            var = Integer.parseInt(j.hget(AccountProvider.REDIS_KEY + uuid, "kitKey"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setKitKey(int kitKey) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "kitKey", String.valueOf(kitKey));
        } finally {
            j.close();
        }
        return this;
    }

    public int getLevel() {
        Jedis j = null;
        int var;
        try {
            j = JedisAccess.get().getResource();
            var = Integer.parseInt(j.hget(AccountProvider.REDIS_KEY + uuid, "level"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setLevel(int level) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "level", String.valueOf(level));
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
        setLevel(getLevel() + level);
        return this;
    }

    public double getXp() {
        Jedis j = null;
        double var;
        try {
            j = JedisAccess.get().getResource();
            var = Double.parseDouble(j.hget(AccountProvider.REDIS_KEY + uuid, "xp"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account addXp(Double xp) {
        setXp(getXp() + xp);
        checkLevel();
        return this;
    }

    public Account removeXp(Double xp) {
        setXp(getXp() + xp);
        checkLevel();
        return this;
    }

    public Account setXp(double xp) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "xp", String.valueOf(xp));
        } finally {
            j.close();
        }
        checkLevel();
        return this;
    }

    private void checkLevel() {
        if (getXp() >= xpToLevelSup()) {
            removeXp(xpToLevelSup());
            addLevel(1);
            checkLevel();
        }
    }

    public double getBooster() {
        Jedis j = null;
        double var;
        try {
            j = JedisAccess.get().getResource();
            var = Double.parseDouble(j.hget(AccountProvider.REDIS_KEY + uuid, "booster"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account setBooster(double booster) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "booster", String.valueOf(booster));
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
        double var;
        try {
            j = JedisAccess.get().getResource();
            var = Double.parseDouble(j.hget(AccountProvider.REDIS_KEY + uuid, "solde"));
        } finally {
            j.close();
        }
        return var;
    }

    public Account addMoney(double value) {
        setSolde(getSolde() + value);
        return this;
    }

    public Account addMoneyWithBooster(double value) {
        addMoney(value * getRealBooster());
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
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "solde", String.valueOf(solde));
        } finally {
            j.close();
        }
        return this;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public Account setUuid(String uuid) {
        this.uuid = uuid;
        return this;
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
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(getLevelWithXp());
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
        String var;
        try {
            j = JedisAccess.get().getResource();
            var = j.hget(AccountProvider.REDIS_KEY + uuid, "selectedKit");
        } finally {
            j.close();
        }
        return var;
    }

    public Account setSelectedKit(String selectedKit) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "selectedKit", String.valueOf(selectedKit));
        } finally {
            j.close();
        }
        return this;
    }

    public String getEffectsString() {
        Jedis j = null;
        String var;
        try {
            j = JedisAccess.get().getResource();
            var = j.hget(AccountProvider.REDIS_KEY + uuid, "effects");
        } finally {
            j.close();
        }
        return var;
    }

    public String getSelectedEffect() {
        Jedis j = null;
        String var;
        try {
            j = JedisAccess.get().getResource();
            var = j.hget(AccountProvider.REDIS_KEY + uuid, "selectedEffect");
        } finally {
            j.close();
        }
        return var;
    }

    public Account setSelectedEffect(String selectedEffect) {
        Jedis j = null;
        try {
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "selectedEffect", String.valueOf(selectedEffect));
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
            j = JedisAccess.get().getResource();
            j.hset(AccountProvider.REDIS_KEY + uuid, "effects", effects.toString().replace(", ", ",").replace("[", "").replace("]", ""));
        } finally {
            j.close();
        }
        return this;
    }

    public Account addEffect(String effect) {
        List<String> effects = getEffects();
        effects.add(effect);
        setEffects(effects);
        return this;
    }

    public Account removeEffect(String effect) {
        List<String> effects = getEffects();
        effects.remove(effect);
        setEffects(effects);
        return this;
    }

    public double getRealBooster() {
        double booster = getBooster();
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player != null) {
            if (player.hasPermission("group.general")) {
                booster += 1;

            }
            else if (player.hasPermission("group.officer")) {
                booster += 0.5;
            }
        }
        return booster;
    }

    public AccountProperties getProperties() {
        return new AccountProperties(UUID.fromString(uuid));
    }
}
