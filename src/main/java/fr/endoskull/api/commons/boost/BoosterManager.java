package fr.endoskull.api.commons.boost;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.endoskull.api.commons.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BoosterManager {
    private Booster booster;
    private Account account;

    public BoosterManager(Booster booster, Account account) {
        this.booster = booster;
        this.account = account;
    }

    public static void setBooster(Account account, Booster booster) {
        try {
            account.setProperty("booster", new ObjectMapper().writeValueAsString(booster));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static Booster getBooster(Account account) {
        if (!account.getProperty("booster").equalsIgnoreCase("")) {
            try {
                return new ObjectMapper().readValue(account.getProperty("booster"), Booster.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new Booster(account.getUuid().toString(), 0, new ArrayList<>());
    }



    public void checkExpirate() {
        for (TempBooster tempBoost : new ArrayList<>(booster.getTempBoosts())) {
            if (System.currentTimeMillis() > tempBoost.getExpiry()) booster.getTempBoosts().remove(tempBoost);
        }
        save();
    }

    public double getRealBooster() {
        checkExpirate();
        double boost = 1;
        boost += booster.getBoost();
        for (TempBooster tempBoost : booster.getTempBoosts()) {
            boost += tempBoost.getBoost();
        }
        Player player = Bukkit.getPlayer(UUID.fromString(booster.getUuid()));
        if (player != null) {
            if (player.hasPermission("group.hero")) {
                boost += 1;

            }
            else if (player.hasPermission("group.vip")) {
                boost += 0.5;
            }
        }
        return boost;
    }

    public void setBoost(double boost) {
        booster.setBoost(boost);
        save();
    }

    public void addBoost(double boost) {
        booster.setBoost(booster.getBoost() + boost);
        save();
    }

    public void removeBoost(double boost) {
        booster.setBoost(booster.getBoost() - boost);
        save();
    }

    public void addTempBoost(TempBooster tempBooster) {
        booster.getTempBoosts().add(tempBooster);
        save();
    }
    public void removeTempBoost(int id) {
        booster.getTempBoosts().remove(id);
        save();
    }

    private void save() {
        BoosterManager.setBooster(account, booster);
    }

    public double getBoost() {
        double boost = booster.getBoost();
        Player player = Bukkit.getPlayer(UUID.fromString(booster.getUuid()));
        if (player != null) {
            if (player.hasPermission("group.hero")) {
                boost += 1;

            }
            else if (player.hasPermission("group.vip")) {
                boost += 0.5;
            }
        }
        return boost;
    }

    public Booster getBooster() {
        return booster;
    }
}