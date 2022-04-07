package fr.endoskull.api.data.redis;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.sql.MySQL;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.UUID;

public class JedisManager {
    public static final String TABLE = "accounts";
    public static final String STATS = "stats";
    public static final String PROPERTIES = "properties";

    public static void sendToDatabase() {

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

    public static void checkNoneName(UUID uuid, String name) {
        BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + uuid + "' AND name='none'", rs -> {
            try {
                if (rs.next()) {
                    BungeeMain.getInstance().getMySQL().update("UPDATE " + TABLE + " SET " + "name" + "='" + name + "' WHERE uuid='" + uuid + "'");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendToDatabase(UUID uuid) {
        Account account = AccountProvider.getAccount(uuid);
        System.out.println("Sauvegarde du compte de " + account.getName());

        BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + TABLE + " WHERE uuid='" + account.getUuid() + "'", rs -> {
            try {
                if (rs.next()) {
                    BungeeMain.getInstance().getMySQL().update("UPDATE " + TABLE + " SET " + "name" + "='" + account.getName() + "', level" + "='" + account.getLevel() + "', xp" + "='" + account.getXp() + "', solde" + "='" + account.getSolde() + "', first_join" + "='" + account.getFirstJoin() + "' WHERE uuid='" + account.getUuid() + "'");
                } else {
                    BungeeMain.getInstance().getMySQL().update("INSERT INTO " + TABLE + " (uuid, name, level, xp, solde, first_join) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getSolde() + "', '" + account.getFirstJoin() + "')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        BungeeMain.getInstance().getMySQL().update("DELETE FROM " + STATS + " WHERE uuid='" + account.getUuid() + "'");
        BungeeMain.getInstance().getMySQL().update("DELETE FROM " + PROPERTIES + " WHERE uuid='" + account.getUuid() + "'");
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            if (j.exists("stats/" + account.getUuid())) {
                j.hgetAll("stats/" + account.getUuid()).forEach((s, s2) -> {
                    BungeeMain.getInstance().getMySQL().update("INSERT INTO `" + STATS + "` (`uuid`, `key`, `value`) VALUES ('" + account.getUuid() + "', '" + s + "', '" + Integer.valueOf(s2) + "')");
                });
            }
            if (j.exists("properties/" + account.getUuid())) {
                j.hgetAll("properties/" + account.getUuid()).forEach((s, s2) -> {
                    BungeeMain.getInstance().getMySQL().update("INSERT INTO `" + PROPERTIES + "` (`uuid`, `key`, `value`) VALUES ('" + account.getUuid() + "', '" + s + "', '" + s2 + "')");
                });
            }
        } finally {
            j.close();
        }
    }

    public static void removeAccountFromRedis(UUID uuid) {
        Jedis j = null;
        try {
            j = JedisAccess.getUserpool().getResource();
            j.del("account/" + uuid);
            j.del("stats/" + uuid);
            j.del("properties/" + uuid);
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
        mySQL.query("SELECT * FROM " + STATS + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                Jedis j = null;
                try {
                    j = JedisAccess.getUserpool().getResource();
                    while(rs.next()){
                        j.hset("stats/" + uuid, rs.getString("key"), String.valueOf(rs.getInt("value")));
                    }
                } finally {
                    j.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
        mySQL.query("SELECT * FROM " + PROPERTIES + " WHERE uuid='" + uuid + "'", rs -> {
            try {
                Jedis j = null;
                try {
                    j = JedisAccess.getUserpool().getResource();
                    while(rs.next()){
                        j.hset("properties/" + uuid, rs.getString("key"), rs.getString("value"));
                    }
                } finally {
                    j.close();
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
