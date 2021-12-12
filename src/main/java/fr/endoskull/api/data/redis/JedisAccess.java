package fr.endoskull.api.data.redis;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.SQLException;
import java.util.UUID;

public class JedisAccess {
    private JedisPool jedisPool;
    private static JedisAccess instance;

    public static JedisAccess getInstance() {
        return instance;
    }

    public JedisAccess(String host, int port, String password) {
        instance = this;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxWaitMillis(5000L);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, 5000, password, 1);
    }

    public static JedisPool get() {
        return JedisAccess.getInstance().getJedisPool();
    }

    public static void init() {
        new JedisAccess("127.0.0.1",6379, "%]h48Ty7UBC?D+439zg%XeV6Pm#k~&9y");
    }

    public static void close() {
        JedisAccess.get().close();
    }

    public static void sendToDatabase() {
        Jedis j = JedisAccess.get().getResource();
        for (String key : j.keys(AccountProvider.REDIS_KEY + "*")) {
            Account account = new AccountProvider(UUID.fromString(key.substring(AccountProvider.REDIS_KEY.length()))).getAccount();
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

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
