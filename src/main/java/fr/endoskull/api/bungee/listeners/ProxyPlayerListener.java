package fr.endoskull.api.bungee.listeners;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.MaintenanceUtils;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;

public class ProxyPlayerListener implements Listener {


    @EventHandler
    public void onLogin(LoginEvent e){

        if(MaintenanceUtils.isInMaintenance("Global") && !MaintenanceUtils.isWhitelisted(e.getConnection().getName())){
            e.setCancelled(true);
            e.setCancelReason(new ComponentBuilder("§c§lEndoSkull Maintenance\n\n§7Plus d'infos §8» §adiscord.endoskull.fr").create());
        }
    }

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
                    JedisManager.checkNoneName(player.getUniqueId(), player.getName());
                    account.setProperty("lastLogin", String.valueOf(System.currentTimeMillis()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                player.disconnect(new TextComponent("§cImpossible de trouver ou créer votre compte"));
            }
        });
        for (UUID friendUUID : FriendUtils.getFriends(player.getUniqueId())) {
            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(friendUUID);
            if (friend == null) continue;
            if (!FriendUtils.getSetting(player.getUniqueId(), FriendSettingsBungee.FRIEND_NOTIFICATION).equalsIgnoreCase("1")) continue;
            friend.sendMessage(new TextComponent("§a§lAMIS §8» " + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §7vient de se §aconnecter").toLegacyText());
        }
        EndoSkullAPI.loadPrefix(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        Account account = AccountProvider.getAccount(player.getUniqueId());
        account.setProperty("lastLogout", String.valueOf(System.currentTimeMillis()));
        if (account.getProperty("vanished", "false").equalsIgnoreCase("true")) account.setProperty("vanished", "false");
        if (JedisManager.isLoad(player.getUniqueId())) AccountProvider.unloadAccount(player.getUniqueId());
        for (UUID friendUUID : FriendUtils.getFriends(player.getUniqueId())) {
            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(friendUUID);
            if (friend == null) continue;
            if (!FriendUtils.getSetting(player.getUniqueId(), FriendSettingsBungee.FRIEND_NOTIFICATION).equalsIgnoreCase("1")) continue;
            friend.sendMessage(new TextComponent("§a§lAMIS §8» " + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §7vient de se §cdéconnecter").toLegacyText());
        }
        BungeeMain.getInstance().getLastPM().remove(player);
        for (ProxiedPlayer p : new ArrayList<>(BungeeMain.getInstance().getLastPM().keySet())) {
            if (BungeeMain.getInstance().getLastPM().get(p).equals(player)) BungeeMain.getInstance().getLastPM().remove(p);
        }
    }
}
