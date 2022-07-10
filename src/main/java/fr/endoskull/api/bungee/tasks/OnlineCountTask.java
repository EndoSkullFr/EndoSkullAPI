package fr.endoskull.api.bungee.tasks;

import fr.endoskull.api.data.sql.MySQL;

import java.sql.SQLException;

public class OnlineCountTask implements Runnable {

    private static String[] tasks = {"Lobby", "PvpKit", "Bedwars"};
    private static int i = 0;
    @Override
    public void run() {
        if (i <= 0) {
            i = 60;
            /*MySQL.getInstance().query("SELECT COUNT(uuid) FROM `accounts`", rs -> {
                try {
                    if (rs.next()) {
                        MySQL.getInstance().update("INSERT INTO `account_count` (`accounts`) VALUES ('" + rs.getLong("COUNT(uuid)") + "')");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });*/
        }
        i--;
        /*for (String task : tasks) {
            final int onlineCount = ServiceInfoSnapshotUtil.getTaskOnlineCount(task);
            MySQL.getInstance().update("INSERT INTO `online_count`(`service`, `online`) VALUES ('" + task + "','" + onlineCount + "')");
        }*/
    }
}
