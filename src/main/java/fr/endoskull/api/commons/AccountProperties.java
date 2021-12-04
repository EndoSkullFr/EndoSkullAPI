package fr.endoskull.api.commons;

import fr.bebedlastreat.cache.CacheAPI;

import java.util.UUID;

public class AccountProperties {
    private UUID uuid;

    public AccountProperties(UUID uuid) {
        this.uuid = uuid;
    }

    public void setProperty(String key, String value) {
        CacheAPI.set("properties/" + uuid + "/" + key, value);
    }

    public String getProperty(String name) {
        return CacheAPI.get("properties/" + uuid + "/" + name);
    }

    public int getInt(String name) {
        String value = getProperty(name);
        if (value == null) return 0;
        return Integer.parseInt(value);
    }

    public double getDouble(String name) {
        String value = getProperty(name);
        if (value == null) return 0;
        return Double.parseDouble(value);
    }
}
