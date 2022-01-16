package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.data.redis.RedisAccess;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClassementTask implements Runnable {
    private Main main;
    private static List<Account> classement = new ArrayList<>();

    public ClassementTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        List<Account> accounts = new ArrayList<>();

        main.getMySQL().query("SELECT `uuid`,`name`,`level`,`xp` FROM `accounts` ORDER BY `level` DESC, `xp` DESC", rs -> {
            try {
                while (rs.next()){
                    accounts.add(new Account(rs.getString("uuid"), rs.getString("name"), 0, 0, 0, 0, rs.getInt("level"), rs.getDouble("xp"), 0, 0, "", "", ""));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        if (Bukkit.getPluginManager().getPlugin("EndoSkullPvpKit") != null && false) {
            Classement.setClassement(accounts);
            if (!Classement.isInit()) Classement.init();
            Classement.update();
        }
        classement = accounts;

    }

    public static List<Account> getClassement() {
        return classement;
    }
}
