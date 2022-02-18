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
                Account account = AccountProvider.getAccount(UUID.fromString(s.substring(8)));
                System.out.println("Sauvegarde du comtpe de " + account.getName());

                BungeeMain.getInstance().getMySQL().query("SELECT * FROM " + AccountProvider.TABLE + " WHERE uuid='" + account.getUuid() + "'", rs -> {
                    try {
                        if (rs.next()) {
                            BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" + account.getName() + "', voteKey" + "='" + account.getVoteKey() + "', ultimeKey" + "='" + account.getUltimeKey() + "', coinsKey" + "='" + account.getCoinsKey() + "', kitKey" + "='" + account.getKitKey() + "', level" + "='" + account.getLevel() + "', xp" + "='" + account.getXp() + "', booster" + "='" + account.getBooster() + "', solde" + "='" + account.getSolde() + "', kit_selected" + "='" + account.getSelectedKit() + "', effects" + "='" + account.getEffectsString() + "', effect_selected" + "='" + account.getSelectedEffect() + "', discord" + "='" + account.getDiscord() + "' WHERE uuid='" + account.getUuid() + "'");
                        } else {
                            BungeeMain.getInstance().getMySQL().update("INSERT INTO " + AccountProvider.TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kit_selected, effects, effect_selected, discord) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getVoteKey() + "', '" + account.getUltimeKey() + "', '" + account.getCoinsKey() + "', '" + account.getKitKey() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getBooster() + "', '" + account.getSolde() + "', '" + account.getSelectedKit() + "', '" + account.getEffectsString() + "', '" + account.getSelectedEffect() + "', '" + account.getDiscord() + "')");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
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
                    BungeeMain.getInstance().getMySQL().update("UPDATE " + AccountProvider.TABLE + " SET " + "name" + "='" + account.getName() + "', voteKey" + "='" + account.getVoteKey() + "', ultimeKey" + "='" + account.getUltimeKey() + "', coinsKey" + "='" + account.getCoinsKey() + "', kitKey" + "='" + account.getKitKey() + "', level" + "='" + account.getLevel() + "', xp" + "='" + account.getXp() + "', booster" + "='" + account.getBooster() + "', solde" + "='" + account.getSolde() + "', kit_selected" + "='" + account.getSelectedKit() + "', effects" + "='" + account.getEffectsString() + "', effect_selected" + "='" + account.getSelectedEffect() + "', discord" + "='" + account.getDiscord() + "' WHERE uuid='" + account.getUuid() + "'");
                } else {
                    BungeeMain.getInstance().getMySQL().update("INSERT INTO " + AccountProvider.TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kit_selected, effects, effect_selected, discord) VALUES ('" + account.getUuid() + "', '" + account.getName() + "', '" + account.getVoteKey() + "', '" + account.getUltimeKey() + "', '" + account.getCoinsKey() + "', '" + account.getKitKey() + "', '" + account.getLevel() + "', '" + account.getXp() + "', '" + account.getBooster() + "', '" + account.getSolde() + "', '" + account.getSelectedKit() + "', '" + account.getEffectsString() + "', '" + account.getSelectedEffect() + "', '" + account.getDiscord() + "')");
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
                    //, rs.getString("name"), rs.getInt("voteKey"), rs.getInt("ultimeKey"), rs.getInt("coinsKey"), rs.getInt("kitKey"), rs.getInt("level"), rs.getDouble("xp"), rs.getDouble("booster"), rs.getDouble("solde"), rs.getString("kit_selected"), rs.getString("effects"), rs.getString("effect_selected"));
                    account.setName(rs.getString("name")).setVoteKey(rs.getInt("voteKey")).setUltimeKey(rs.getInt("ultimeKey")).setLevel(rs.getInt("level"))
                            .setXp(rs.getDouble("xp")).setBooster(rs.getDouble("booster")).setSolde(rs.getDouble("solde")).setSelectedKit(rs.getString("kit_selected"))
                            .setEffects(rs.getString("effects")).setSelectedEffect(rs.getString("effect_selected")).setDiscord(rs.getString("discord"));
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
        mySQL.update("INSERT INTO " + TABLE + " (uuid, name, voteKey, ultimeKey, coinsKey, kitKey, level, xp, booster, solde, kit_selected, effects, effect_selected, discord) VALUES ('" + uuid + "', '" + "none" + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + 1 + "', '" + 0 + "', '" + "" + "', '" + "" + "', '" + "" + "', '" + "" + "')");
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
