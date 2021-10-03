package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.commons.Account;

import java.util.Comparator;

public class LevelComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        if (o1.getLevelWithXp() > o2.getLevelWithXp()) return -1;
        if (o1.getLevelWithXp() == o2.getLevelWithXp()) return 0;
        return 1;
    }
}
