package fr.endoskull.api.data.redis;

import fr.bebedlastreat.cache.CacheAPI;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.sql.MySQL;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;

public class JedisManager {
    public static final String TABLE = "accounts";

    public static void sendToDatabase() {
        CacheAPI.set("lastSend", System.currentTimeMillis());


        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            for (String s : j.keys("account/*")) {
                sendToDatabase(UUID.fromString(s.substring(8)));
            }
        } finally {
            j.close();
        }

    }

    public static void sendToDatabase(UUID uuid) {
        Account account = AccountProvider.getAccount(uuid);
        System.out.println("Sauvegarde du comtpe de " + account.getName());

        BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + AccountProvider.TABLE + " WHERE uuid='" + account.getUuid() + "'", rs -> {
            try {
                if (rs.next()) {
                    BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" + account.getName() + "', level" + "='" + account.getLevel() + "', xp" + "='" + account.getXp() + "', solde" + "='" + account.getSolde() + "', first_join" + "='" + account.getFirstJoin() + "' WHERE uuid='" + account.getUuid() + "'");
                } else {
                    BungeeMain.getInstance().getMySQL().update("INSERT INTO " + AccountProvider.TABLE + " (uuid, name, level, xp, solde, first_join) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getSolde() + "', '" + account.getFirstJoin() + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void removeAccountFromRedis(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.del("account/" + uuid);
        } finally {
            j.close();
        }
    }

    public static void loadFromDatabase(UUID uuid) {
        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                if(rs.next()){
                    Account account = new Account(UUID.fromString(rs.getString("uuid")));
                    account.setName(rs.getString("name")).setLevel(rs.getInt("level"))
                            .setXp(rs.getDouble("xp")).setSolde(rs.getDouble("solde"))
                            .setFirstJoin(rs.getLong("first_join"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static boolean accountExist(UUID uuid) {
        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        return (boolean) mySQL.query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static void createNewAccount(UUID uuid) {

        MySQL mySQL;
        try {
            mySQL = BungeeMain.getInstance().getMySQL();
        } catch (NoClassDefFoundError e) {
            mySQL = Main.getInstance().getMySQL();
        }
        mySQL.update("INSERT INTO " + TABLE + " (uuid, name, level, xp, solde, first_join) VALUES ('" + uuid + "', '" + "none" + "', '" + 1 + "', '" + 0 + "', '" + 100 + "', '" + System.currentTimeMillis() + "')");
    }

    public static boolean isLoad(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            return j.exists("account/" + uuid);
        } finally {
            j.close();
        }
    }
}
