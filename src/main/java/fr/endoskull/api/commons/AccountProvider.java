package fr.endoskull.api.commons;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.RedisAccess;
import fr.endoskull.api.data.sql.MySQL;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.SQLException;
import java.util.UUID;

public class AccountProvider {
    public static final String REDIS_KEY = "account:";
    public static final String TABLE = "accounts";
    public static final Account DEFAULT_ACCOUNT = new Account(UUID.randomUUID().toString(), "none", 0, 0, 0, 0, 1, 0, 1, 0, "", "");

    private RedisAccess redisAccess;
    private UUID uuid;

    public AccountProvider(UUID uuid) {
        this.uuid = uuid;
        this.redisAccess = RedisAccess.instance;
    }

    public Account getAccount() {
        Account account = getAccountFromRedis();
        if (account == null) {
            account = getAccountFromDatabase();
            sendAccountToRedis(account);
        }

        return account;
    }

    public void sendAccountToRedis(Account account) {
        final RedissonClient redisson = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final  RBucket<Account> accountRBucket = redisson.getBucket(key);
        accountRBucket.set(account);
    }

    private Account getAccountFromRedis() {
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);
        return accountRBucket.get();
    }

    private Account getAccountFromDatabase() {
        //get account
        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        return (Account) mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    return new Account(rs.getString("uuid"), rs.getString("name"), rs.getInt("voteKey"), rs.getInt("ultimeKey"), rs.getInt("coinsKey"), rs.getInt("kitKey"), rs.getInt("level"), rs.getDouble("xp"), rs.getDouble("booster"), rs.getDouble("solde"), rs.getString("kits"), rs.getString("kit_selected"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return createNewAccount();
        });
        //si non trouver createNewAccount();
    }

    private Account createNewAccount() {
        //add to data base
        final Account account = DEFAULT_ACCOUNT.clone();
        account.setUuid(uuid.toString());

        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        mySQL.update("INSERT INTO " + TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kits, kit_selected) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getVoteKey() + "', '" + account.getUltimeKey() + "', '" + account.getCoinsKey() + "', '" + account.getKitKey() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getBooster() + "', '" + account.getSolde() + "', '" + account.getKitsString() + "', '" + account.getSelectedKit() + "')");

        return account;
    }
}
