package fr.endoskull.api.spigot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class TabListener implements Listener {

    @EventHandler
    public void onPlayerClickTab(PlayerChatTabCompleteEvent e)
    {
        if (e.getPlayer().getName().equalsIgnoreCase("BebeDlaStreat")) {
            Player player = e.getPlayer();
            player.sendMessage(e.getChatMessage());
            player.sendMessage(e.getLastToken());

        }
        //e.getTabCompletions().clear();
        //e.getChatMessage().charAt(0);
    }

}
