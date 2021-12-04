package fr.endoskull.api.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.bebedlastreat.cache.CacheAPI;
import fr.endoskull.api.data.redis.RedisAccess;
import org.redisson.RedissonBucket;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class AccountProperties {
    private UUID uuid;

    RedissonClient redisson;

    public AccountProperties(UUID uuid) {
        this.uuid = uuid;
        redisson = RedisAccess.instance.getRedissonClient();
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
