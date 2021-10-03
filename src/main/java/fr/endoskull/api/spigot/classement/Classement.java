package fr.endoskull.api.spigot.classement;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import fr.endoskull.api.commons.Account;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Classement {
    private static boolean init = false;
    private static List<Account> classement = new ArrayList<>();
    private static Location loc = new Location(Bukkit.getWorld("world"), 2.5, 133.5, -7.5);
    private static Hologram holo0;
    private static Hologram holo1;
    private static Hologram holo2;
    private static Hologram holo3;
    private static Hologram holo4;
    private static Hologram holo5;
    private static Hologram holo6;
    private static Hologram holo7;
    private static Hologram holo8;
    private static Hologram holo9;
    private static Hologram holo10;

    public static void init() {
        init = true;
        holo0 = HologramAPI.createHologram(loc.clone(), "§f§m       §6§l Classement Top XP §f§m       ");
        holo0.spawn();
        holo1 = HologramAPI.createHologram(loc.clone().add(0, -0.5, 0), "");
        holo1.spawn();
        holo2 = holo1.addLineBelow("");
        holo3 = holo2.addLineBelow("");
        holo4 = holo3.addLineBelow("");
        holo5 = holo4.addLineBelow("");
        holo6 = holo5.addLineBelow("");
        holo7 = holo6.addLineBelow("");
        holo8 = holo7.addLineBelow("");
        holo9 = holo8.addLineBelow("");
        holo10 = holo9.addLineBelow("");

    }

    public static void update() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        UserManager userManager = luckPerms.getUserManager();

        if (classement.size() < 1) return;
        Account account1 = classement.get(0);
        CompletableFuture<User> userFuture1 = userManager.loadUser(account1.getUuid());
        userFuture1.thenAcceptAsync(user1 -> {
            String prefix1 = user1.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
            holo1.setText("§e➊ §7⋙ " + prefix1 + " " + account1.getName() + " §7⋙ " + "§7Lvl §a" + account1.getStringLevel());

            if (classement.size() < 2) return;
            Account account2 = classement.get(1);
            CompletableFuture<User> userFuture2 = userManager.loadUser(account2.getUuid());
            userFuture2.thenAcceptAsync(user2 -> {
                String prefix2 = user2.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                holo2.setText("§7❷ §7⋙ " + prefix2 + " " + account2.getName() + " §7⋙ " + "§7Lvl §a" + account2.getStringLevel());

                if (classement.size() < 3) return;
                Account account3 = classement.get(2);
                CompletableFuture<User> userFuture3 = userManager.loadUser(account3.getUuid());
                userFuture3.thenAcceptAsync(user3 -> {
                    String prefix3 = user3.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                    holo3.setText("§6❸ §7⋙ " + prefix3 + " " + account3.getName() + " §7⋙ " + "§7Lvl §a" + account3.getStringLevel());

                    if (classement.size() < 4) return;
                    Account account4 = classement.get(3);
                    CompletableFuture<User> userFuture4 = userManager.loadUser(account4.getUuid());
                    userFuture4.thenAcceptAsync(user4 -> {
                        String prefix4 = user4.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                        holo4.setText("§f➍ §7⋙ " + prefix4 + " " + account4.getName() + " §7⋙ " + "§7Lvl §a" + account4.getStringLevel());

                        if (classement.size() < 5) return;
                        Account account5 = classement.get(4);
                        CompletableFuture<User> userFuture5 = userManager.loadUser(account5.getUuid());
                        userFuture5.thenAcceptAsync(user5 -> {
                            String prefix5 = user5.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                            holo5.setText("§f➎ §7⋙ " + prefix5 + " " + account5.getName() + " §7⋙ " + "§7Lvl §a" + account5.getStringLevel());

                            if (classement.size() < 6) return;
                            Account account6 = classement.get(5);
                            CompletableFuture<User> userFuture6 = userManager.loadUser(account6.getUuid());
                            userFuture6.thenAcceptAsync(user6 -> {
                                String prefix6 = user6.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                                holo6.setText("§f➏ §7⋙ " + prefix6 + " " + account6.getName() + " §7⋙ " + "§7Lvl §a" + account6.getStringLevel());

                                if (classement.size() < 7) return;
                                Account account7 = classement.get(6);
                                CompletableFuture<User> userFuture7 = userManager.loadUser(account7.getUuid());
                                userFuture7.thenAcceptAsync(user7 -> {
                                    String prefix7 = user7.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                                    holo7.setText("§f➐ §7⋙ " + prefix7 + " " + account7.getName() + " §7⋙ " + "§7Lvl §a" + account7.getStringLevel());

                                    if (classement.size() < 8) return;
                                    Account account8 = classement.get(7);
                                    CompletableFuture<User> userFuture8 = userManager.loadUser(account8.getUuid());
                                    userFuture8.thenAcceptAsync(user8 -> {
                                        String prefix8 = user8.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                                        holo8.setText("§f➑ §7⋙ " + prefix8 + " " + account8.getName() + " §7⋙ " + "§7Lvl §a" + account8.getStringLevel());

                                        if (classement.size() < 9) return;
                                        Account account9 = classement.get(8);
                                        CompletableFuture<User> userFuture9 = userManager.loadUser(account9.getUuid());
                                        userFuture9.thenAcceptAsync(user9 -> {
                                            String prefix9 = user9.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                                            holo9.setText("§f➒ §7⋙ " + prefix9 + " " + account9.getName() + " §7⋙ " + "§7Lvl §a" + account9.getStringLevel());

                                            if (classement.size() < 10) return;
                                            Account account10 = classement.get(9);
                                            CompletableFuture<User> userFuture10 = userManager.loadUser(account10.getUuid());
                                            userFuture10.thenAcceptAsync(user10 -> {
                                                String prefix10 = user10.getCachedData().getMetaData().getPrefix().substring(0, 2).replace("&", "§");
                                                holo10.setText("§f➓ §7⋙ " + prefix10 + " " + account10.getName() + " §7⋙ " + "§7Lvl §a" + account10.getStringLevel());
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }

    public static List<Account> getClassement() {
        return classement;
    }

    public static void setClassement(List<Account> classement) {
        Classement.classement = classement;
    }

    public static boolean isInit() {
        return init;
    }
}
