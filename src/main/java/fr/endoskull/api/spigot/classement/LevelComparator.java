package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.commons.Account;

import java.util.Comparator;

public class LevelComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        return Double.compare(o2.getLevelWithXp(), o1.getLevelWithXp());
    }
}
