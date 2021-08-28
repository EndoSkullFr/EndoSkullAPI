package fr.endoskull.api.tasks;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import fr.endoskull.api.Main;
import fr.endoskull.api.database.Level;
import fr.endoskull.api.utils.EndoSkullPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            EndoSkullPlayer skullPlayer = new EndoSkullPlayer(pls);
            ActionBarAPI.sendActionBar(pls, "§eLevel : §6" + skullPlayer.getLevel() + " §f| §eXp : §6" + skullPlayer.getXp() + "§e/§6" + skullPlayer.xpToLevelSup());
        }

    }
}
