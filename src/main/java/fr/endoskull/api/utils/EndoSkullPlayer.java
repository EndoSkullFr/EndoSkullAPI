package fr.endoskull.api.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.database.Account;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.database.Level;
import fr.endoskull.api.database.Money;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class EndoSkullPlayer {
    private UUID uuid;

    public EndoSkullPlayer(Player player) {
        if (!Main.getInstance().getUuidsByName().containsKey(player.getName())) {
            this.uuid = null;
            return;
        }
        this.uuid = Main.getInstance().getUuidsByName().get(player.getName());
    }
    public EndoSkullPlayer(String name) {
        if (!Main.getInstance().getUuidsByName().containsKey(name)) {
            this.uuid = null;
            return;
        }
        this.uuid = Main.getInstance().getUuidsByName().get(name);
    }
    public EndoSkullPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isNull() {
        return uuid == null;
    }

    public int getLevel() {
        return Level.getLevel(uuid);
    }
    public void setLevel(int number) {
        Level.setLevel(uuid, number);
    }
    public void addLevel(int number) {
        setLevel(getLevel() + number);
    }
    public void removeLevel(int number) {
        setLevel(getLevel() + number);
    }
    public double getXp() {
        return Level.getXp(uuid);
    }
    public void setXp(double number) {
        Level.setXp(uuid, number);
    }
    public void addXp(double number) {
        setXp(getXp() + number);
    }
    public void removeXp(double number) {
        setXp(getXp() + number);
    }
    public double xpToLevelSup() {
        return 20d + (getLevel() * 10d);
    }

    public List<RankUnit> getRankList() {
        return Account.getRankList(uuid);
    }
    public RankUnit getRank() {
        RankUnit rank = RankUnit.JOUEUR;
        for (RankUnit r : getRankList()) {
            if (r.getPower() < rank.getPower()) {
                rank = r;
            }
        }
        return rank;
    }
    public boolean hasRank(RankUnit rankUnit) {
        return getRankList().contains(rankUnit);
    }
    public void addRank(RankUnit rankUnit) {
        Account.addRank(rankUnit, uuid);
    }
    public void removeRank(RankUnit rankUnit) {
        Account.removeRank(rankUnit, uuid);
    }

    public int getKey(String name) {
        return Keys.getKeys(uuid, name);
    }
    public void setKey(String name, int number) {
        Keys.setKey(uuid, name, number);
    }
    public void addKey(String name, int number) {
        setKey(name, getKey(name) + number);
    }
    public void removeKey(String name, int number) {
        setKey(name, getKey(name) - number);
    }

    public double getMoney() {
        return Money.getMoney(uuid);
    }
    public void setMoney(double number) {
        Money.setMoney(uuid, number);
    }
    public void addMoney(double number) {
        setMoney(getMoney() + number);
    }
    public void removeMoney(double number) {
        setMoney(getMoney() - number);
    }
}
