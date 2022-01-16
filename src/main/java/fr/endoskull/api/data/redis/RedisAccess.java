package fr.endoskull.api.data.redis;

import fr.bebedlastreat.cache.CacheAPI;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.sql.SQLException;
import java.util.Iterator;

public class RedisAccess {
    private RedissonClient redissonClient;
    public static RedisAccess instance;

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public RedisAccess(RedisCredentials redisCredentials) {
        instance = this;
        this.redissonClient = initRedisson(redisCredentials);
    }

    public static void init() {
        new RedisAccess(new RedisCredentials("127.0.0.1", "%]h48Ty7UBC?D+439zg%XeV6Pm#k~&9y", 6379));
    }

    public static void close() {
        RedisAccess.instance.getRedissonClient().shutdown();
    }
    public static void sendToDatabase() {
        CacheAPI.set("lastSend", System.currentTimeMillis());
        RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();
        Iterator<String> iterator = redissonClient.getKeys().getKeys().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith("account:")) {
                RBucket<Account> accountRBucket = redissonClient.getBucket(s);
                Account account = accountRBucket.get();
                System.out.println("Sauvegarde du comtpe de " + account.getName());

                BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + AccountProvider.TABLE + " WHERE uuid='" + account.getUuid() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" + account.getName() + "', voteKey" + "='" + account.getVoteKey() + "', ultimeKey" + "='" + account.getUltimeKey() + "', coinsKey" + "='" + account.getCoinsKey() + "', kitKey" + "='" + account.getKitKey() + "', level" + "='" + account.getLevel() + "', xp" + "='" + account.getXp() + "', booster" + "='" + account.getBooster() + "', solde" + "='" + account.getSolde() + "', kit_selected" + "='" + account.getSelectedKit() + "', effects" + "='" + account.getEffectsString() + "', effect_selected" + "='" + account.getSelectedEffect() + "' WHERE uuid='" + account.getUuid() + "'");
                        } else {
                            BungeeMain.getInstance().getMySQL().update("INSERT INTO " + AccountProvider.TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kit_selected, effects, effect_selected) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getVoteKey() + "', '" + account.getUltimeKey() + "', '" + account.getCoinsKey() + "', '" + account.getKitKey() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getBooster() + "', '" + account.getSolde() + "', '" + account.getSelectedKit() + "', '" + account.getEffectsString() + "', '" + account.getSelectedEffect() + "')");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public RedissonClient initRedisson(RedisCredentials redisCredentials) {
        final Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        //config.setUseLinuxNativeEpoll(true);
        config.setThreads(6);
        config.setNettyThreads(6);
        config.useSingleServer()
                .setAddress(redisCredentials.toRedisUrl())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(0)
                .setClientName(redisCredentials.getClientName());
        return Redisson.create(config);
    }
}
