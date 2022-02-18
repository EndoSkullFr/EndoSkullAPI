package fr.endoskull.api.bungee.listeners;

import de.simonsator.partyandfriends.main.listener.PlayerDisconnectListener;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPlayerListener implements Listener {

    @EventHandler
    public void onProxyJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getInstance(), () -> {
            try {
                if (!JedisManager.isLoad(player.getUniqueId())) {
                    AccountProvider.loadAccount(player.getUniqueId());
                    AccountProvider accountProvider = new AccountProvider(player.getUniqueId());
                    Account account = accountProvider.getAccount();
                    account.setName(player.getName());
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                player.disconnect(new TextComponent("§cImpossible de trouver ou créer votre compte"));
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        AccountProvider.unloadAccount(player.getUniqueId());
    }
}
