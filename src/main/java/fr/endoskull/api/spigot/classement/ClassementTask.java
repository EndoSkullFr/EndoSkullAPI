package fr.endoskull.api.spigot.classement;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.ClassementAccount;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClassementTask implements Runnable {
    private Main main;
    private static List<ClassementAccount> classement = new ArrayList<>();

    public ClassementTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        List<ClassementAccount> accounts = new ArrayList<>();

        main.getMySQL().query("SELECT `uuid`,`name`,`level`,`xp` FROM `accounts` ORDER BY `level` DESC, `xp` DESC", rs -> {
            try {
                while (rs.next()){
                    accounts.add(new ClassementAccount(UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getInt("level"), rs.getDouble("xp")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        classement = accounts;

    }

    public static List<ClassementAccount> getClassement() {
        return classement;
    }
}
