package fr.endoskull.api.spigot.listeners;

import de.musterbukkit.replaysystem.main.ReplayAPI;
import fr.endoskull.api.commons.miscellaneous.DiscordWebhook;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;
import java.io.IOException;

public class AntiCheatListener implements Listener {

    @EventHandler
    public void onPunish(VulcanPunishEvent e) {
        Player player = e.getPlayer();
        System.out.println("Event called");
        System.out.println(player.getName() + " got punish by vulcan");

        String snapshot = ReplayAPI.createSnapshotPlayer(120, player);
        System.out.println("Snapshot " + snapshot);
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/983354998584913951/ZBIGUC1aKZr80zDaKmP5L1TNw4ohYKu9pG0fIvUW7PkkAPCety4KGomDh95P7P9QZal5");
        webhook.setAvatarUrl("https://www.spigotmc.org/data/resource_icons/49/49554.jpg?1510601303");
        webhook.setUsername("Replay AntiCheat");
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setColor(Color.ORANGE);
        embedObject.setTitle(player.getName());
        embedObject.setDescription("Id du replay: `" + snapshot + "`\\nAlerte: `" + e.getCheck().getDisplayName() + " " + e.getCheck().getType() + "`");
        webhook.addEmbed(embedObject);
        try {
            webhook.execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
