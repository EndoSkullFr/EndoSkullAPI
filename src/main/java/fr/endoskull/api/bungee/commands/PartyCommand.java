package fr.endoskull.api.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.EndoSkullAPI;
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

    /**
     *
     * Faire un run task later
     * Sauvegarder l'id de la task dans une map
     * si friend accepter cancel la task
     * a la fin de la task envoyer message de refus
     *
     * ou
     *
     * faire une task toutes les secondes
     * avec une maap
     * si accepter retirer de la map
     * si temps écouler envoyer message
     *
     */

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 2 && args[0].equalsIgnoreCase("accept")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (!PartyUtils.hasRequest(targetUUID, player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'avez pas de demande de partie de la part de ce joueur\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous êtes déjà dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }

            Party party = PartyUtils.getParty(targetUUID);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §avient de rejoindre la partie\n" + EndoSkullAPI.LINE).toLegacyText());
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez rejoint la partie de " + EndoSkullAPI.getPrefix(targetUUID) + targetName + "\n" + EndoSkullAPI.LINE).toLegacyText());
            party.getMembers().add(player.getUniqueId());
            PartyUtils.saveParty(party);
            PAFTask.getPartyRequests().get(targetUUID).remove(player.getUniqueId());
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("invite")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous ne pouvez pas vous inviter dans votre propre\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if (target == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'est pas connecté\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (PartyUtils.hasRequest(player.getUniqueId(), target.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous êtes déjà une demande de partie avec ce joueur\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (FriendUtils.getSetting(target.getUniqueId(), FriendSettingsBungee.PARTY_REQUEST).equalsIgnoreCase("0") && !FriendUtils.areFriends(player.getUniqueId(), target.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être ami avec ce joueur pour l'inviter dans votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (PartyUtils.isInParty(target.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur est déjà dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party;
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                party = new Party(player.getUniqueId(), new ArrayList<>(), new HashMap<>());
                PartyUtils.saveParty(party);
            } else {
                party = PartyUtils.getParty(player.getUniqueId());
                if (!party.getLeader().equals(player.getUniqueId())) {
                    player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                            EndoSkullAPI.LINE).toLegacyText());
                    return;
                }
            }
            PartyUtils.addRequest(player.getUniqueId(), target.getUniqueId());
            TextComponent message = new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getColor(player.getUniqueId()) + player.getName() + " §avient de vous inviter dans la partie\n");
            TextComponent accept = new TextComponent("§a[Accepter]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + player.getName()));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aCliquez pour accepter").create()));
            message.addExtra(accept);
            message.addExtra("\n" + EndoSkullAPI.LINE);
            target.sendMessage(message);
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez invité " + EndoSkullAPI.getPrefix(target.getUniqueId()) + target.getName() + " §adans votre partie\n" + EndoSkullAPI.LINE));
            for (UUID memberUUID : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(memberUUID);
                if (member == null) continue;
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(target.getUniqueId()) + target.getName() + " §avient d'être invité dans votre partie\n" + EndoSkullAPI.LINE).toLegacyText());
            }
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" + EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            StringBuilder partyMessage = new StringBuilder();
            partyMessage.append(EndoSkullAPI.LINE).append("\n");
            String leaderName = BungeePlayerInfos.getNameFromUuid(party.getLeader());
            partyMessage.append("§7Chef: " + EndoSkullAPI.getColor(party.getLeader()) + leaderName).append("\n");
            List<String> members = new ArrayList<>();
            for (UUID member : party.getMembers()) {
                members.add(EndoSkullAPI.getColor(member) + BungeePlayerInfos.getNameFromUuid(member));
            }
            partyMessage.append("§7Membre(s): " + members.toString().replace("[", "").replace("]", "").replace(",", ", ")).append("\n");
            partyMessage.append(EndoSkullAPI.LINE);

            player.sendMessage(new TextComponent(partyMessage.toString()).toLegacyText());
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("leave")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous êtes le chef de votre partie, faîtes /party transfer (Pseudo) avant\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            party.getMembers().remove(player.getUniqueId());
            PartyUtils.saveParty(party);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §cvient de quitter la partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez quitté votre partie\n" + EndoSkullAPI.LINE).toLegacyText());
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("disband")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §cvient de dissoudre la partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§eVous avez dissout la partie\n" + EndoSkullAPI.LINE).toLegacyText());
            PartyUtils.removeParty(party);
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("warp")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.connect(player.getServer().getInfo());
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §cvient de vous envoyer sur son serveur\n" +
                        EndoSkullAPI.LINE).toLegacyText());
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez envoyer les joueurs de votre partie sur votre serveur\n" + EndoSkullAPI.LINE).toLegacyText());
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("transfer")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName()) && false) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous ne pouvez pas vous transférer la partie à vous même\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (!party.getMembers().contains(targetUUID)) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'est pas dans votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
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
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(targetUUID) + targetName + " §aest le nouveau chef de la partie\n" + EndoSkullAPI.LINE).toLegacyText());
            }
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("kick")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName()) && false) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous ne pouvez pas vous transférer la partie à vous même\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (!party.getMembers().contains(targetUUID)) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'est pas dans votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            party.getMembers().remove(targetUUID);
            PartyUtils.saveParty(party);
            for (UUID uuid : party.getPlayers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) continue;
                member.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(targetUUID) + targetName + " §cvient d'être exclu de la partie\n" + EndoSkullAPI.LINE).toLegacyText());
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous avez été exclu de la partie\n" + EndoSkullAPI.LINE).toLegacyText());
            }
            return;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("kickoffline")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (!party.getLeader().equals(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous devez être le chef de votre partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            List<UUID> offlines = new ArrayList<>();
            for (UUID uuid : party.getMembers()) {
                ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                if (member == null) offlines.add(uuid);
            }
            if (offlines.isEmpty()) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cTous les joueurs de la partie sont en lignes\n" +
                        EndoSkullAPI.LINE).toLegacyText());
            } else {
                party.getMembers().removeAll(offlines);
                PartyUtils.saveParty(party);
                for (UUID uuid : party.getPlayers()) {
                    ProxiedPlayer member = ProxyServer.getInstance().getPlayer(uuid);
                    if (member == null) continue;
                    player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§e" + offlines.size() +  " §7joueur(s) hors ligne(s) viennent d'être exclu(s) de la partie\n" +
                            EndoSkullAPI.LINE).toLegacyText());
                }
            }
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("chat")) {
            if (!PartyUtils.isInParty(player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas dans une partie\n" +
                        EndoSkullAPI.LINE).toLegacyText());
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
                member.sendMessage(new TextComponent("§a§lPARTIE §8» " + EndoSkullAPI.getColor(player.getUniqueId()) + player.getName() + " §7» §f" + message).toLegacyText());
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


        player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aCommandes de parties:\n" +
                "§e/party accept (Pseudo) §8- §7Accepter une demande de partie\n" +
                "§e/party invite (Pseudo) §8- §7Inviter un joueur dans sa partie\n" +
                "§e/party list §8- §7Afficher les membres de votre partie\n" +
                "§e/party leave §8- §7Quitter votre partie\n" +
                "§e/party warp §8- §7Envoyer les joueurs de votre partie sur votre serveur\n" +
                "§e/party disband §8- §7Dissoudre votre partie\n" +
                "§e/party transfer (Pseudo) §8- §7Transférer votre partie\n" +
                "§e/party kick (Pseudo) §8- §7Retirer un joueur de la partie\n" +
                "§e/party kickoffline §8- §7Retirer les joueurs déconnectés de la parties\n" +
                "§e/party chat (Message) §8- §7Envoyer un message à tous les joueurs de la partie\n" +
                "§e/party settings §8- §7Ouvrir les paramètres liés aux parties\n" +
                EndoSkullAPI.LINE).toLegacyText());
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
