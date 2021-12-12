package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.redis.JedisAccess;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassementTask implements Runnable {
    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();
        Jedis j = JedisAccess.get().getResource();
        for (String key : j.keys(AccountProvider.REDIS_KEY + "*")) {
            accounts.add(new Account(key.substring(AccountProvider.REDIS_KEY.length()), j.hget(AccountProvider.REDIS_KEY + key.substring(AccountProvider.REDIS_KEY.length()), "name")));
        }
        //Collections.sort(accounts, new LevelComparator());
        List<Account> topAccounts = new ArrayList<>();
        boolean boucle = true;
        while (boucle) {
            Account topAccount = null;
            for (Account account : accounts) {
                if (topAccount == null) {
                    topAccount = account;
                } else if (topAccount.getLevelWithXp() < account.getLevelWithXp()) {
                    topAccount = account;
                }
            }
            if (topAccount == null) {
                boucle = false;
            }
            accounts.remove(topAccount);
            topAccounts.add(topAccount);
        }
        Classement.setClassement(topAccounts);
        if (!Classement.isInit()) Classement.init();
        Classement.update();

    }
}
