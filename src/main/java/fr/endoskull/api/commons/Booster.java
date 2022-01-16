package fr.endoskull.api.commons;

import java.util.List;

public class Booster {
    private String uuid;
    private double boost;
    private List<TempBooster> tempBoosts;

    public Booster(){}

    public Booster(String uuid, double boost, List<TempBooster> tempBoosts) {
        this.uuid = uuid;
        this.boost = boost;
        this.tempBoosts = tempBoosts;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public List<TempBooster> getTempBoosts() {
        return tempBoosts;
    }

    public void setTempBoosts(List<TempBooster> tempBoosts) {
        this.tempBoosts = tempBoosts;
    }
}
