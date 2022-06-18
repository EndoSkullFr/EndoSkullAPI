package fr.endoskull.api.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendSettingsSpigot;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.commons.EndoSkullAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class FriendCommand extends Command implements TabExecutor {
    public FriendCommand() {
        super("friend", "", "friends", "f");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BungeeLang lang = BungeeLang.getLang(sender);
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            List<UUID> friendsUUID = FriendUtils.getOrderedFriends(player.getUniqueId());
            if (friendsUUID.isEmpty()) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ANY_FRIEND)));
                return;
            }
            StringBuilder friendsMessage = new StringBuilder();
            for (UUID uuid : friendsUUID) {
                String name = BungeePlayerInfos.getNameFromUuid(uuid);
                boolean online = ProxyServer.getInstance().getPlayer(name) != null;
                friendsMessage.append("§7" + EndoSkullAPI.getPrefix(uuid) + name + " §8» " + (online ? lang.getMessage(MessageUtils.Paf.ONLINE) : lang.getMessage(MessageUtils.Paf.OFFLINE)) + "\n");
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n                    " + lang.getMessage(MessageUtils.Paf.FLIST) + "\n" + friendsMessage + EndoSkullAPI.LINE));
            return;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.FRIEND_SELF)));
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER)));
                return;
            }
            if (FriendUtils.areFriends(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ALREADY_FRIEND)));
                return;
            }
            if (FriendUtils.hasRequestFrom(player.getUniqueId(), targetUUID)) {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "friend accept " + targetName);
                return;
            }
            if (FriendUtils.hasRequestFrom(targetUUID, player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ALREADY_FREQUEST)));
                return;
            }

            if (!FriendUtils.getSetting(targetUUID, FriendSettingsBungee.FRIEND_REQUEST).equalsIgnoreCase("1")) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.FREQUESTS_DISABLE)));
                return;
            }

            FriendUtils.addRequest(player.getUniqueId(), targetUUID);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                TextComponent message = new TextComponent(EndoSkullAPI.LINE + "\n" + BungeeLang.getLang(target).getMessage(MessageUtils.Paf.FREQUEST_RECEIVE).replace("{sender}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName()) + "\n");
                TextComponent accept = new TextComponent("§a[" + BungeeLang.getLang(target).getMessage(MessageUtils.Global.ACCEPT) + "]");
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(BungeeLang.getLang(target).getMessage(MessageUtils.Paf.CLICK_ACCEPT)).create()));
                message.addExtra(accept);
                message.addExtra("\n" + EndoSkullAPI.LINE);
                target.sendMessage(message);
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.FREQUEST_SEND).replace("{target}", EndoSkullAPI.getPrefix(targetUUID) + targetName)));
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER) + "\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (!FriendUtils.areFriends(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NOT_FRIEND)));
                return;
            }
            FriendUtils.removeFriend(player.getUniqueId(), targetUUID);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(BungeeLang.getLang(target).getMessage(MessageUtils.Paf.FRIEND_REMOVED).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())));
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.FRIEND_REMOVE).replace("{target}", EndoSkullAPI.getPrefix(targetUUID) + targetName)));
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("accept")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER) + "\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (!FriendUtils.hasRequestFrom(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_FREQUEST)).toLegacyText());
                return;
            }
            FriendUtils.removeRequest(targetUUID, player.getUniqueId());
            FriendUtils.addFriend(targetUUID, player.getUniqueId());
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NOW_FRIEND).replace("{player}", EndoSkullAPI.getPrefix(targetUUID) + targetName)).toLegacyText());
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(BungeeLang.getLang(target).getMessage(MessageUtils.Paf.FRIEND_ACCEPT).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
            }
            PAFTask.getFriendRequests().get(targetUUID).remove(player.getUniqueId());
            return;
        }


        if (args.length >= 1 && args[0].equalsIgnoreCase("settings")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("OpenSettings");

            BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), () -> {
                player.getServer().sendData(BungeeMain.CHANNEL, out.toByteArray());
            }, 0, TimeUnit.SECONDS);
            return;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("requests")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("OpenRequests");

            BungeeMain.getInstance().getProxy().getScheduler().schedule(BungeeMain.getInstance(), () -> {
                player.getServer().sendData(BungeeMain.CHANNEL, out.toByteArray());
            }, 0, TimeUnit.SECONDS);
            return;
        }

        player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.FRIEND_HELP)).toLegacyText());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String s : Arrays.asList("accept", "add", "list", "settings", "requests")) {
                if (s.startsWith(args[0])) result.add(s);
            }
            return result;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("add"))) {
            List<String> result = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().startsWith(args[1])) result.add(player.getName());
            }
            return result;
        }
        return new ArrayList<>();
    }
}
