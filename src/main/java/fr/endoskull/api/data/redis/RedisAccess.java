package fr.endoskull.api.data.redis;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import io.netty.util.internal.PlatformDependent;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

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
        RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();
        Iterator<String> iterator = redissonClient.getKeys().getKeys().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith("account:")) {
                RBucket<Account> accountRBucket = redissonClient.getBucket(s);
                Account account = accountRBucket.get();
                BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" +  account.getName() + "', voteKey" + "='" +  account.getVoteKey()  + "', ultimeKey" + "='" +  account.getUltimeKey() + "', coinsKey" + "='" +  account.getCoinsKey() + "', kitKey" + "='" +  account.getKitKey() + "', level" + "='" +  account.getLevel() + "', xp" + "='" +  account.getXp() + "', booster" + "='" +  account.getVoteKey() + "', solde" + "='" +  account.getSolde() + "', kits" + "='" +  account.getKitsString() + "', kit_selected" + "='" + account.getSelectedKit() + "' WHERE uuid='" + account.getUuid() + "'");
            }
        }
        System.out.println(redissonClient.getKeys().getKeys().iterator().next());
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
