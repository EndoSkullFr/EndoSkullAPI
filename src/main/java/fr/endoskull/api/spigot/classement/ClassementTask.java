package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.redis.RedisAccess;
import org.bukkit.scheduler.BukkitRunnable;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ClassementTask implements Runnable {
    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();
        RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();
        Iterator<String> iterator = redissonClient.getKeys().getKeys().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (s.startsWith("account:")) {
                RBucket<Account> accountRBucket = redissonClient.getBucket(s);
                accounts.add(accountRBucket.get());
            }
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
