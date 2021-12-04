package fr.endoskull.api.commons;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.Main;
import fr.endoskull.api.data.redis.RedisAccess;
import fr.endoskull.api.data.sql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AccountProvider {
    public static final String REDIS_KEY = "account:";
    public static final String TABLE = "accounts";

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

        return new Account(uuid.toString(), RedisAccess.get().hget("account:" + uuid, "name"));
    }

    private boolean loadAccountFromRedis() {
        return RedisAccess.get().exists("account:" + uuid);
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
            Jedis jedis = RedisAccess.get();
            try {
                if(rs.next()){
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    jedis.hset("account:" + uuid, "name", rs.getString("name"));
                    jedis.hset("account:" + uuid, "voteKey", String.valueOf(rs.getInt("voteKey")));
                    jedis.hset("account:" + uuid, "ultimeKey", String.valueOf(rs.getInt("ultimeKey")));
                    jedis.hset("account:" + uuid, "coinsKey", String.valueOf(rs.getInt("coinsKey")));
                    jedis.hset("account:" + uuid, "kitKey", String.valueOf(rs.getInt("kitKey")));
                    jedis.hset("account:" + uuid, "level", String.valueOf(rs.getInt("level")));
                    jedis.hset("account:" + uuid, "xp", String.valueOf(rs.getDouble("xp")));
                    jedis.hset("account:" + uuid, "booster", String.valueOf(rs.getDouble("booster")));
                    jedis.hset("account:" + uuid, "solde", String.valueOf(rs.getDouble("solde")));
                    jedis.hset("account:" + uuid, "kit_selected", rs.getString("kit_selected"));
                    jedis.hset("account:" + uuid, "effects", rs.getString("effects"));
                    jedis.hset("account:" + uuid, "effect_selected", rs.getString("effect_selected"));
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
        //si non trouver createNewAccount();
    }

    private void createNewAccount() {

        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        mySQL.update("INSERT INTO " + TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kits, kit_selected, effects, effect_selected) VALUES ('" +
                uuid + "', '" + "none" + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + "" + "', '" + "" + "', '" + "" + "')");
        try {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                loadAccountFromDatabase();
            }, 20);
        } catch (Exception e) {
            ProxyServer.getInstance().getScheduler().schedule(BungeeMain.getInstance(), () -> {
                loadAccountFromDatabase();
            }, 1, TimeUnit.SECONDS);
        }
    }
}
