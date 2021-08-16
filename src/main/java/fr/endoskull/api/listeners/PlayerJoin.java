package fr.endoskull.api.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import fr.endoskull.api.Main;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.database.Level;
import fr.endoskull.api.tasks.BossBarRunnable;
import fr.endoskull.api.utils.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

public class PlayerJoin implements Listener {
    private Main main;
    public PlayerJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("UUID");
                player.sendPluginMessage(main, "BungeeCord", b.toByteArray());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //UUID uuid = PlayerInfos.getUuidFromName(player.getName());
            //if (!main.getUuidsByName().containsKey(player)) main.getUuidsByName().put(player.getName(), uuid);
            UUID uuid= player.getUniqueId();
            EndoSkullPlayer skullPlayer = new EndoSkullPlayer(player);
            Level.setup(uuid);
            Keys.setup(uuid);
            if (main.getConfig().getBoolean("bossbar")) {
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    String message ="§eLevel : §6" + skullPlayer.getLevel() + " §f| §eXp : §6" + skullPlayer.getXp() + "§e/§6" + skullPlayer.xpToLevelSup();
                    float health = (float) (skullPlayer.getXp() / skullPlayer.xpToLevelSup());
                    BossBar bossBar = BossBarAPI.addBar(player, // The receiver of the BossBar
                            new TextComponent(message), // Displayed message
                            BossBarAPI.Color.BLUE, // Color of the bar
                            BossBarAPI.Style.PROGRESS, // Bar style
                            health); // Timeout-interval
                    BossBarRunnable.getBars().put(player, bossBar);
                }, 20);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (BossBarRunnable.getBars().containsKey(player)) {
            BossBarRunnable.getBars().get(player).removePlayer(player);
            BossBarAPI.removeAllBars(player);
        }
    }
}
