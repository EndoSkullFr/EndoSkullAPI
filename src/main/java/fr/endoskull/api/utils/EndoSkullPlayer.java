package fr.endoskull.api.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.database.Level;
import fr.endoskull.api.database.Money;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class EndoSkullPlayer {
    private UUID uuid;

    public EndoSkullPlayer(Player player) {
        this.uuid = player.getUniqueId();
    }
    public EndoSkullPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            this.uuid = null;
            return;
        }
        this.uuid = player.getUniqueId();
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
    public double getLevelWithXp() {
        double level = getLevel();
        double xp = Double.valueOf(Math.round(getXp() / xpToLevelSup() * 10)) / 10;
        return level + xp;
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
