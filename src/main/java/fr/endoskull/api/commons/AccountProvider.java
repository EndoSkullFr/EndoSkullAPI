package fr.endoskull.api.commons;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.api.data.sql.MySQL;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.UUID;

public class AccountProvider {
    public static final String REDIS_KEY = "account:";
    public static final String TABLE = "accounts";
    //public static final Account DEFAULT_ACCOUNT = new Account(UUID.randomUUID().toString(), "none", 0, 0, 0, 0, 1, 0, 1, 0, "", "", "");

    private UUID uuid;

    public AccountProvider(UUID uuid) {
        this.uuid = uuid;
    }

    public static Account getAccount(UUID uuid) {
        return new AccountProvider(uuid).getAccount();
    }

    public Account getAccount() {
        if (!loadAccountFromRedis()) {
            if (!loadAccountFromDatabase()) {
                createNewAccount();
            }
        }

        return new Account(uuid.toString(), JedisAccess.get().getResource().hget(REDIS_KEY + uuid, "name"));
    }

    private boolean loadAccountFromRedis() {
        return JedisAccess.get().getResource().exists(REDIS_KEY + uuid);
    }

    private boolean loadAccountFromDatabase() {
        //get account
        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        return (boolean) mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    Jedis j = null;
                    try {
                        j = JedisAccess.get().getResource();
                        j.hset(AccountProvider.REDIS_KEY + uuid, "name", rs.getString("name"));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "voteKey", String.valueOf(rs.getInt("voteKey")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "ultimeKey", String.valueOf(rs.getInt("ultimeKey")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "coinsKey", String.valueOf(rs.getInt("coinsKey")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "kitKey", String.valueOf(rs.getInt("kitKey")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "level", String.valueOf(rs.getInt("level")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "xp", String.valueOf(rs.getDouble("xp")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "booster", String.valueOf(rs.getDouble("booster")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "solde", String.valueOf(rs.getDouble("solde")));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "kit_selected", rs.getString("kit_selected"));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "effects", rs.getString("effects"));
                        j.hset(AccountProvider.REDIS_KEY + uuid, "effect_selected", rs.getString("effect_selected"));
                    } finally {
                        j.close();
                    }
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    private void createNewAccount() {
        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        mySQL.update("INSERT INTO " + TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kit_selected, effects, effect_selected) VALUES ('" + uuid + "', '" + "none" + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + "" + "', '" + "" + "', '" + "" + "')");
        loadAccountFromDatabase();
    }
}
