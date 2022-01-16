package fr.endoskull.api.commons;

public class TempBooster {
    private double boost;
    private long expiry;

    public TempBooster(double boost, long expiry) {
        this.boost = boost;
        this.expiry = expiry;
    }

    public TempBooster() {}

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }
}
