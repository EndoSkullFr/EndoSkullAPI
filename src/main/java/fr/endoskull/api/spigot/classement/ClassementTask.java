package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.redis.RedisAccess;

import java.util.*;

public class ClassementTask implements Runnable {
    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();
        for (String s : RedisAccess.get().keys("account:*")) {
            s = s.substring(8);
            accounts.add(AccountProvider.getAccount(UUID.fromString(s)));
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
