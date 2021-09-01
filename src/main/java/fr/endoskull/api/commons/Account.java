package fr.endoskull.api.commons;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class Account implements Cloneable {
    private String uuid;
    private String name;
    private int voteKey;
    private int ultimeKey;
    private int coinsKey;
    private int kitKey;
    private int level;
    private double xp;
    private double booster;
    private double solde;

    public Account() {}

    public Account(String uuid, String name, int voteKey, int ultimeKey, int coinsKey, int kitKey, int level, double xp, double booster, double solde) {
        this.uuid = uuid;
        this.name = name;
        this.voteKey = voteKey;
        this.ultimeKey = ultimeKey;
        this.coinsKey = coinsKey;
        this.kitKey = kitKey;
        this.level = level;
        this.xp = xp;
        this.booster = booster;
        this.solde = solde;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public int getVoteKey() {
        return voteKey;
    }

    public Account setVoteKey(int voteKey) {
        this.voteKey = voteKey;
        return this;
    }

    public int getUltimeKey() {
        return ultimeKey;
    }

    public Account setUltimeKey(int ultimeKey) {
        this.ultimeKey = ultimeKey;
        return this;
    }

    public int getCoinsKey() {
        return coinsKey;
    }

    public Account setCoinsKey(int coinsKey) {
        this.coinsKey = coinsKey;
        return this;
    }

    public int getKitKey() {
        return kitKey;
    }

    public Account setKitKey(int kitKey) {
        this.kitKey = kitKey;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Account setLevel(int level) {
        this.level = level;
        return this;
    }

    public double getXp() {
        return xp;
    }

    public Account setXp(double xp) {
        this.xp = xp;
        checkLevel();
        return this;
    }

    private void checkLevel() {
        if (xp >= xpToLevelSup()) {
            xp -= xpToLevelSup();
            level++;
            checkLevel();
        }
    }

    public double getBooster() {
        return booster;
    }

    public Account setBooster(double booster) {
        this.booster = booster;
        return this;
    }

    public String getStringBooster() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(0);
        return df.format(booster);
    }


    public double getSolde() {
        return solde;
    }

    public String getStringSolde() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(0);
        return df.format(solde);
    }

    public Account setSolde(double solde) {
        this.solde = solde;
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
        return df.format(getLevelWithXp());
    }

    public void sendToRedis() {
        AccountProvider accountProvider = new AccountProvider(UUID.fromString(uuid));
        accountProvider.sendAccountToRedis(this);
    }
}
