package fr.endoskull.api.commons.nick;

import fr.endoskull.api.data.redis.JedisAccess;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class NickData {

    public static void nick(UUID uuid, String name, String value, String signature) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.hset("nick/" + uuid, "name", name);
            j.hset("nick/" + uuid, "value", value);
            j.hset("nick/" + uuid, "signature", signature);
        } finally {
            j.close();
        }
    }

    public static void unnick(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.del("nick/" + uuid);
        } finally {
            j.close();
        }
    }

    public static boolean isNicked(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.exists("nick/" + uuid);
        } finally {
            j.close();
        }
    }

    public static String[] getNickInfo(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return new String[]{j.hget("nick/" + uuid, "name"), j.hget("nick/" + uuid, "value"), j.hget("nick/" + uuid, "signature")};
        } finally {
            j.close();
        }
    }
}
