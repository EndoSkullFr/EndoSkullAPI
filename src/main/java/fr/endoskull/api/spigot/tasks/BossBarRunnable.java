package fr.endoskull.api.spigot.tasks;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
            sendActionBar(pls, "§eLevel : §6" + account.getLevel() + " §f| §eXp : §6" + account.getXp() + "§e/§6" + account.xpToLevelSup());
        }

    }

    public void sendActionBar(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
