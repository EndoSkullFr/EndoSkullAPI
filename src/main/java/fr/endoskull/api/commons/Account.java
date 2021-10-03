package fr.endoskull.api.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private List<String> kits;
    private String selectedKit;
    private List<String> effects;
    private String selectedEffect;

    public Account() {}

    public Account(String uuid, String name, int voteKey, int ultimeKey, int coinsKey, int kitKey, int level, double xp, double booster, double solde, String kitsString, String selectedKit, String effectsString, String selectedEffect) {
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
        this.kits = stringToArray(kitsString);
        this.selectedKit = selectedKit;
        this.effects = stringToArray(effectsString);
        this.selectedEffect = selectedEffect;
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
        df.setGroupingUsed(false);
        return df.format(getRealBooster());
    }


    public double getSolde() {
        return solde;
    }

    public String getStringSolde() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        if (solde > 1000000) {
            return df.format(solde/1000000) + "M";
        }
        if (solde > 1000) {
            return df.format(solde/1000) + "k";
        }
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
        df.setGroupingUsed(false);
        return df.format(getLevelWithXp());
    }

    public void sendToRedis() {
        AccountProvider accountProvider = new AccountProvider(UUID.fromString(uuid));
        accountProvider.sendAccountToRedis(this);
    }

    public List<String> getKits() {
        return kits;
    }

    public Account setKits(List<String> kits) {
        this.kits = kits;
        return this;
    }

    public Account addKit(String kit) {
        kits.add(kit);
        return this;
    }

    public Account removeKit(String kit) {
        kits.remove(kit);
        return this;
    }


    private List<String> stringToArray(String kitsString) {
        if (kitsString.length() == 0) return new ArrayList<>();
        if (kitsString.contains(",")) {
            return Arrays.asList(kitsString.split(","));
        } else {
            return Arrays.asList(kitsString);
        }
    }

    public String getKitsString() {
        return kits.toString().replace(", ", ",").replace("[", "").replace("]", "");
    }

    public String getSelectedKit() {
        return selectedKit;
    }

    public Account setSelectedKit(String selectedKit) {
        this.selectedKit = selectedKit;
        return this;
    }

    public String getEffectsString() {
        return effects.toString().replace(", ", ",").replace("[", "").replace("]", "");
    }

    public String getSelectedEffect() {
        return selectedEffect;
    }

    public Account setSelectedEffect(String selectedEffect) {
        this.selectedEffect = selectedEffect;
        return this;
    }

    public List<String> getEffects() {
        return effects;
    }

    public Account setEffects(List<String> effects) {
        this.effects = effects;
        return this;
    }

    public Account addEffect(String effect) {
        effects.add(effect);
        return this;
    }

    public Account removeEffect(String effect) {
        effects.remove(effect);
        return this;
    }

    public double getRealBooster() {
        double booster = this.booster;
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player != null) {
            if (player.hasPermission("group.general")) {
                booster += 4;

            }
            else if (player.hasPermission("group.officer")) {
                booster += 2;
            }
        }
        return booster;
    }
}
