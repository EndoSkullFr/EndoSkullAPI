package fr.endoskull.api.spigot.tasks;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarRunnable implements Runnable {

    private static Main main;
    public BossBarRunnable(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        /*for (Player player : bars.keySet()) {
            EndoSkullPlayer skullPlayer = new EndoSkullPlayer(player);
            String message ="§eLevel : §6" + skullPlayer.getLevel() + " §f| §eXp : §6" + skullPlayer.getXp() + "§e/§6" + skullPlayer.xpToLevelSup();
            float health = (float) (skullPlayer.getXp() / skullPlayer.xpToLevelSup());
            BossBarAPI.removeAllBars(player);
            BossBar bossBar = BossBarAPI.addBar(player, // The receiver of the BossBar
                    new TextComponent(message), // Displayed message
                    BossBarAPI.Color.BLUE, // Color of the bar
                    BossBarAPI.Style.PROGRESS, // Bar style
                    health);
            bars.put(player, bossBar);
        }*/
        for (Player pls : Bukkit.getOnlinePlayers()) {
            Account account = new AccountProvider(pls.getUniqueId()).getAccount();
            ActionBarAPI.sendActionBar(pls, "§eLevel : §6" + account.getLevel() + " §f| §eXp : §6" + account.getXp() + "§e/§6" + account.xpToLevelSup());
        }

    }
}
