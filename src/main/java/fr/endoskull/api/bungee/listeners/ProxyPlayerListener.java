package fr.endoskull.api.bungee.listeners;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.commons.paf.Party;
import fr.endoskull.api.commons.paf.PartyUtils;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
                    JedisManager.checkNoneName(player.getUniqueId(), player.getName());
                    account.setProperty("lastLogin", String.valueOf(System.currentTimeMillis()));
                    BungeeMain.getLangs().put(player, account.getBungeeLang());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                player.disconnect(new TextComponent("§cAccount Loading Error"));
            }
        });
        for (UUID friendUUID : FriendUtils.getFriends(player.getUniqueId())) {
            ProxiedPlayer friend = ProxyServer.getInstance().getPlayer(friendUUID);
            if (friend == null) continue;
            if (!FriendUtils.getSetting(player.getUniqueId(), FriendSettingsBungee.FRIEND_NOTIFICATION).equalsIgnoreCase("1")) continue;
            BungeeLang lang = BungeeLang.getLang(friend);
            friend.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.FRIENDS) + lang.getMessage(MessageUtils.Paf.FRIEND_CONNECT).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
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
            BungeeLang lang = BungeeLang.getLang(friend);
            friend.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.FRIENDS) + lang.getMessage(MessageUtils.Paf.FRIEND_DISCONNECT).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
        }
        BungeeMain.getInstance().getLastPM().remove(player);
        for (ProxiedPlayer p : new ArrayList<>(BungeeMain.getInstance().getLastPM().keySet())) {
            if (BungeeMain.getInstance().getLastPM().get(p).equals(player)) BungeeMain.getInstance().getLastPM().remove(p);
        }
        BungeeMain.getLangs().remove(player);
    }

    @EventHandler
    public void onSwitch(ServerConnectEvent e) {
        ProxiedPlayer player = e.getPlayer();
        if (e.getTarget().getName().startsWith(ServerType.LOBBY.getServerName())) return;
        boolean warp = false;
        String name = e.getTarget().getName();
        if (!name.contains("-")) return;
        name = name.split("-")[0];
        for (ServerType value : ServerType.values()) {
            if (value.getServerName().equalsIgnoreCase(name)) warp = true;
        }
        if (!warp) return;
        if (PartyUtils.isInParty(player.getUniqueId())) {
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (party.getLeader().equals(player.getUniqueId())) {
                BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), () -> {
                    for (UUID uuid : party.getMembers()) {
                        ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                        if (member == null) continue;
                        member.connect(player.getServer().getInfo());
                    }
                }, 500, TimeUnit.MILLISECONDS);
            }
        }
    }
}
