package fr.endoskull.api.listeners;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import fr.endoskull.api.Main;
import fr.endoskull.api.database.Keys;
import fr.endoskull.api.database.Level;
import fr.endoskull.api.database.Money;
import fr.endoskull.api.tasks.BossBarRunnable;
import fr.endoskull.api.utils.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            Money.setup(uuid);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
    }
}
