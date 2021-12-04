package fr.endoskull.api.data.redis;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;

public class RedisAccess {
    private Jedis jedis;
    private static RedisAccess instance;

    public static RedisAccess getInstance() {
        return instance;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public static Jedis get() {
        return RedisAccess.getInstance().getJedis();
    }

    public RedisAccess(String host, String password, int port) {
        instance = this;
        jedis = new Jedis(host, port);
        jedis.auth(password);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(0);
        jedisPoolConfig.setMaxTotal(10000);
    }

    public static void init() {
        new RedisAccess("127.0.0.1", "%]h48Ty7UBC?D+439zg%XeV6Pm#k~&9y", 6379);
    }

    public static void close() {
        RedisAccess.getInstance().get().close();
    }

    public static void sendToDatabase() {
        /*RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();
        Iterator<String> iterator = redissonClient.getKeys().getKeys().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith("account:")) {
                RBucket<Account> accountRBucket = redissonClient.getBucket(s);
                Account account = accountRBucket.get();
                BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" +  account.getName() + "', voteKey" + "='" +  account.getVoteKey()  + "', ultimeKey" + "='" +  account.getUltimeKey() + "', coinsKey" + "='" +  account.getCoinsKey() + "', kitKey" + "='" +  account.getKitKey() + "', level" + "='" +  account.getLevel() + "', xp" + "='" +  account.getXp() + "', booster" + "='" +  account.getBooster() + "', solde" + "='" +  account.getSolde() + "', kit_selected" + "='" + account.getSelectedKit() + "', effects" + "='" +  account.getEffectsString() + "', effect_selected" + "='" + account.getSelectedEffect() + "' WHERE uuid='" + account.getUuid() + "'");
            }
        }*/
    }
}
