package fr.endoskull.api.commons;

import fr.endoskull.api.data.redis.RedisAccess;
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
    private Jedis jedis;

    public Account() {}

    public Account(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.jedis = RedisAccess.get();
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        jedis.hset("account:" + uuid, "name", name);
        return this;
    }

    public int getVoteKey() {
        return Integer.parseInt(jedis.hget("account:" + uuid, "voteKey"));
    }

    public Account setVoteKey(int voteKey) {
        jedis.hset("account:" + uuid, "voteKey", String.valueOf(voteKey));
        return this;
    }

    public int getUltimeKey() {
        return Integer.parseInt(jedis.hget("account:" + uuid, "ultimeKey"));
    }

    public Account setUltimeKey(int ultimeKey) {
        jedis.hset("account:" + uuid, "ultimeKey", String.valueOf(ultimeKey));
        return this;
    }

    public int getCoinsKey() {
        return Integer.parseInt(jedis.hget("account:" + uuid, "coinsKey"));
    }

    public Account setCoinsKey(int coinsKey) {
        jedis.hset("account:" + uuid, "coinsKey", String.valueOf(coinsKey));
        return this;
    }

    public int getKitKey() {
        return Integer.parseInt(jedis.hget("account:" + uuid, "kitKey"));
    }

    public Account setKitKey(int kitKey) {
        jedis.hset("account:" + uuid, "kitKey", String.valueOf(kitKey));
        return this;
    }

    public int getLevel() {
        return Integer.parseInt(jedis.hget("account:" + uuid, "level"));
    }

    public Account setLevel(int level) {
        jedis.hset("account:" + uuid, "level", String.valueOf(level));
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
        return Double.parseDouble(jedis.hget("account:" + uuid, "xp"));
    }

    public Account addXp(Double xp) {
        setXp(getXp() + xp);
        checkLevel();
        return this;
    }

    public Account removeXp(Double xp) {
        setXp(getXp() - xp);
        checkLevel();
        return this;
    }

    public Account setXp(double xp) {
        jedis.hset("account:" + uuid, "xp", String.valueOf(xp));
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
        return Double.parseDouble(jedis.hget("account:" + uuid, "booster"));
    }

    public Account setBooster(double booster) {
        jedis.hset("account:" + uuid, "booster", String.valueOf(booster));
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
        return Double.parseDouble(jedis.hget("account:" + uuid, "solde"));
    }

    public Account addMoney(double value) {
        setSolde(getSolde() + value);
        return this;
    }

    public Account addMoneyWithBooster(double value) {
        addMoney(value * getBooster());
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
        jedis.hset("account:" + uuid, "solde", String.valueOf(solde));
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
        return jedis.hget("account:" + uuid, "kit_selected");
    }

    public Account setSelectedKit(String selectedKit) {
        jedis.hset("account:" + uuid, "kit_selected", selectedKit);
        return this;
    }

    public String getEffectsString() {
        return getEffects().toString().replace(", ", ",").replace("[", "").replace("]", "");
    }

    public String getSelectedEffect() {
        return jedis.hget("account:" + uuid, "effect_selected");
    }

    public Account setSelectedEffect(String selectedEffect) {
        jedis.hset("account:" + uuid, "effect_selected", selectedEffect);
        return this;
    }

    public List<String> getEffects() {
        return Arrays.asList(jedis.hget("account:" + uuid, "effects").split(","));
    }

    public Account setEffects(List<String> effects) {
        jedis.hset("account:" + uuid, "effects", effects.toString().replace(", ", ",").replace("[", "").replace("]", ""));
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
