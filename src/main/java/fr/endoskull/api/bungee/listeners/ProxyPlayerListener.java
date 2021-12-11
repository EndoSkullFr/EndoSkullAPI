package fr.endoskull.api.bungee.listeners;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.commons.exceptions.AccountNotFoundException;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Locale;

public class ProxyPlayerListener implements Listener {

    @EventHandler
    public void onProxyJoin(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();

        BungeeCord.getInstance().getScheduler().runAsync(BungeeMain.getInstance(), () -> {
            try {
                AccountProvider accountProvider = new AccountProvider(player.getUniqueId());
                Account account = accountProvider.getAccount();
                account.setName(player.getName());

                accountProvider.sendAccountToRedis(account);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                player.disconnect(new TextComponent("§cImpossible de trouver ou créer votre compte"));
            }
        });
    }
}
