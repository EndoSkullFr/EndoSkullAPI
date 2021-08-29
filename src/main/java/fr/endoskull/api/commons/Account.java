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

    public void setName(String name) {
        this.name = name;
    }

    public int getVoteKey() {
        return voteKey;
    }

    public void setVoteKey(int voteKey) {
        this.voteKey = voteKey;
    }

    public int getUltimeKey() {
        return ultimeKey;
    }

    public void setUltimeKey(int ultimeKey) {
        this.ultimeKey = ultimeKey;
    }

    public int getCoinsKey() {
        return coinsKey;
    }

    public void setCoinsKey(int coinsKey) {
        this.coinsKey = coinsKey;
    }

    public int getKitKey() {
        return kitKey;
    }

    public void setKitKey(int kitKey) {
        this.kitKey = kitKey;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getBooster() {
        return booster;
    }

    public void setBooster(double booster) {
        this.booster = booster;
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

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        double xp = Double.valueOf(Math.round(getXp() / xpToLevelSup() * 10)) / 10;
        return level + xp;
    }
}
