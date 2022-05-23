package fr.endoskull.api.commons.account;

import fr.endoskull.api.data.redis.JedisManager;

import java.util.UUID;

public class AccountProvider {
    public static final String REDIS_KEY = "account/";
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
        return new Account(uuid);
    }

    public static void loadAccount(UUID uuid) {
        if (JedisManager.accountExist(uuid)) {
            JedisManager.loadFromDatabase(uuid);
        } else {
            JedisManager.createNewAccount(uuid);
            JedisManager.loadFromDatabase(uuid);
        }
    }

    public static void unloadAccount(UUID uuid) {
        JedisManager.sendToDatabase(uuid);
        JedisManager.removeAccountFromRedis(uuid);
    }
}
