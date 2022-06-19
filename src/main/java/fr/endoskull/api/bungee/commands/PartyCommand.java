package fr.endoskull.api.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.commons.paf.Party;
import fr.endoskull.api.commons.paf.PartyUtils;
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

public class PartyCommand extends Command implements TabExecutor {
    public PartyCommand() {
        super("party", "", "p");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BungeeLang lang = BungeeLang.getLang(sender);
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 2 && args[0].equalsIgnoreCase("accept")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER)));
                return;
            }
            if (!PartyUtils.hasRequest(targetUUID, player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PREQUEST)).toLegacyText());
                return;
            }
            if (PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ALREADY_PARTY)).toLegacyText());
                return;
            }

            Party party = PartyUtils.getParty(targetUUID);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.SOMEONE_JOIN_PARTY).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.YOU_JOIN_PARTY).replace("{player}", EndoSkullAPI.getPrefix(targetUUID) + targetName)).toLegacyText());
            party.getMembers().add(player.getUniqueId());
            PartyUtils.saveParty(party);
            PAFTask.getPartyRequests().get(targetUUID).remove(player.getUniqueId());
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("invite")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PARTY_SELF)).toLegacyText());
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if (target == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.OFFLINE_PLAYER)).toLegacyText());
                return;
            }
            if (PartyUtils.hasRequest(player.getUniqueId(), target.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ALREADY_PREQUEST)).toLegacyText());
                return;
            }
            if (FriendUtils.getSetting(target.getUniqueId(), FriendSettingsBungee.PARTY_REQUEST).equalsIgnoreCase("0") && !FriendUtils.areFriends(player.getUniqueId(), target.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PARTY_DISABLE)).toLegacyText());
                return;
            }
            if (PartyUtils.isInParty(target.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.ALREADY_IN_PARTY)).toLegacyText());
                return;
            }
            Party party;
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                party = new Party(player.getUniqueId(), new ArrayList<>(), new HashMap<>());
                PartyUtils.saveParty(party);
            } else {
                party = PartyUtils.getParty(player.getUniqueId());
                if (!party.getLeader().equals(player.getUniqueId())) {
                    player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                    return;
                }
            }
            PartyUtils.addRequest(player.getUniqueId(), target.getUniqueId());
            TextComponent message = new TextComponent(BungeeLang.getLang(target).getMessage(MessageUtils.Paf.PARTY_INVITE).replace("{player}", EndoSkullAPI.getColor(player.getUniqueId()) + player.getName()));
            TextComponent accept = new TextComponent("§a[" + lang.getMessage(MessageUtils.Global.ACCEPT) + "]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + player.getName()));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lang.getMessage(MessageUtils.Paf.CLICK_ACCEPT)).create()));
            message.addExtra(accept);
            message.addExtra("\n" + EndoSkullAPI.LINE);
            target.sendMessage(message);
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.YOU_PARTY_INVITE).replace("{player}", EndoSkullAPI.getPrefix(target.getUniqueId()) + target.getName())));
            for (UUID memberUUID : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(memberUUID);
                if (member == null) continue;
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.YOUR_PARTY_INVITE).replace("{player}", EndoSkullAPI.getPrefix(target.getUniqueId()) + target.getName())).toLegacyText());
            }
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            StringBuilder partyMessage = new StringBuilder();
            partyMessage.append(EndoSkullAPI.LINE).append("\n");
            String leaderName = BungeePlayerInfos.getNameFromUuid(party.getLeader());
            partyMessage.append(MessageUtils.Paf.PARTY_LEADER + EndoSkullAPI.getColor(party.getLeader()) + leaderName).append("\n");
            List<String> members = new ArrayList<>();
            for (UUID member : party.getMembers()) {
                members.add(EndoSkullAPI.getColor(member) + BungeePlayerInfos.getNameFromUuid(member));
            }
            partyMessage.append(lang.getMessage(MessageUtils.Paf.PARTY_MEMBERS) + members.toString().replace("[", "").replace("]", "").replace(",", ", ")).append("\n");
            partyMessage.append(EndoSkullAPI.LINE);

            player.sendMessage(new TextComponent(partyMessage.toString()).toLegacyText());
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("leave")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PARTY_LEADER_LEAVE)).toLegacyText());
                return;
            }
            party.getMembers().remove(player.getUniqueId());
            PartyUtils.saveParty(party);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.SOMEONE_PLEAVE).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.YOU_PLEAVE)).toLegacyText());
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("disband")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                return;
            }
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.PARTY_DISBAND).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.YOU_PDISBAND)).toLegacyText());
            PartyUtils.removeParty(party);
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("warp")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                return;
            }
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.connect(player.getServer().getInfo());
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.PARTY_WARP).replace("{player}", EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName())).toLegacyText());
            }
            player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.YOU_PWARP)).toLegacyText());
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("transfer")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName()) && false) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PTRANSFER_SELF)).toLegacyText());
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER)));
                return;
            }
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                return;
            }
            if (!party.getMembers().contains(targetUUID)) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NOT_IN_YOUR_PARTY)).toLegacyText());
                return;
            }
            PartyUtils.removeParty(party);
            party.setLeader(targetUUID);
            party.getMembers().remove(targetUUID);
            party.getMembers().add(player.getUniqueId());
            PartyUtils.saveParty(party);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.NEW_PLEADER).replace("{player}", EndoSkullAPI.getPrefix(targetUUID) + targetName)).toLegacyText());
            }
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("kick")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PKICK_SELF)).toLegacyText());
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Global.UNKNOWN_PLAYER)));
                return;
            }
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                return;
            }
            if (!party.getMembers().contains(targetUUID)) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NOT_IN_YOUR_PARTY)).toLegacyText());
                return;
            }
            party.getMembers().remove(targetUUID);
            PartyUtils.saveParty(party);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.SOMEONE_PKICK).replace("{player}", EndoSkullAPI.getPrefix(targetUUID) + targetName)).toLegacyText());
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(BungeeLang.getLang(target).getMessage(MessageUtils.Paf.PARTY_KICK)).toLegacyText());
            }
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("kickoffline")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NEED_PLEADER)).toLegacyText());
                return;
            }
            List<UUID> offlines = new ArrayList<>();
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) offlines.add(uuid);
            }
            if (offlines.isEmpty()) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.EVERYONE_ONLINE)).toLegacyText());
            } else {
                party.getMembers().removeAll(offlines);
                PartyUtils.saveParty(party);
                for (UUID uuid : party.getPlayers()) {
                    ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                    if (member == null) continue;
                    member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.PARTY_KICKOFFLINE).replace("{amount}", String.valueOf(offlines.size()))).toLegacyText());
                }
            }
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("chat")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.NO_PARTY)).toLegacyText());
                return;
            }
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]);
                if (i != args.length - 1) message.append(" ");
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(BungeeLang.getLang(member).getMessage(MessageUtils.Paf.PARTY_CHAT).replace("{player}", EndoSkullAPI.getColor(player.getUniqueId()) + player.getName()).replace("{message}", message)).toLegacyText());
            }
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


        player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.PARTY_HELP)).toLegacyText());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            for (String s : Arrays.asList("accept", "invite", "list", "leave", "warp", "disband", "transfer", "kick", "kickoffline", "chat", "settings")) {
                if (s.startsWith(args[0])) result.add(s);
            }
            return result;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("transfer") || args[0].equalsIgnoreCase("kick"))) {
            List<String> result = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getName().startsWith(args[1])) result.add(player.getName());
            }
            return result;
        }
        return new ArrayList<>();
    }
}
