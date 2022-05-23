package fr.endoskull.api.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.tasks.PAFTask;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "", "friends", "f");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            List<UUID> friendsUUID = FriendUtils.getOrderedFriends(player.getUniqueId());
            if (friendsUUID.isEmpty()) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" +
                        "§cVotre liste d'amis est vide\n" +
                        "§7Pour commencer à en ajouter faîtes §a/friend add (Pseudo\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            StringBuilder friendsMessage = new StringBuilder();
            for (UUID uuid : friendsUUID) {
                String name = BungeePlayerInfos.getNameFromUuid(uuid);
                boolean online = ProxyServer.getInstance().getPlayer(name) != null;
                friendsMessage.append("§7" + EndoSkullAPI.getPrefix(uuid) + name + " §8» " + (online ? "§a(Connecté)" : "§c(Déconnecté)") + "\n");
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n                    §a§lListe d'amis:\n" + friendsMessage + EndoSkullAPI.LINE));
            return;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            String targetName = args[1];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous ne pouvez pas vous ajouter en ami\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (FriendUtils.areFriends(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous êtes déjà amis avec ce joueur\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (FriendUtils.hasRequestFrom(player.getUniqueId(), targetUUID)) {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "friend accept " + targetName);
                return;
            }
            if (FriendUtils.hasRequestFrom(targetUUID, player.getUniqueId())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous avez déjà un demande d'ami en cours avec ce joueur\n" + EndoSkullAPI.LINE));
                return;
            }

            if (!FriendUtils.getSetting(targetUUID, FriendSettingsBungee.FRIEND_REQUEST).equalsIgnoreCase("1")) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur a desactivé ses demandes d'amis\n" + EndoSkullAPI.LINE));
                return;
            }

            FriendUtils.addRequest(player.getUniqueId(), targetUUID);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                TextComponent message = new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " vient de vous envoyer une demande d'ami\n");
                TextComponent accept = new TextComponent("§a[Accepter]");
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + player.getName()));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aCliquez pour accepter").create()));
                message.addExtra(accept);
                message.addExtra("\n" + EndoSkullAPI.LINE);
                target.sendMessage(message);
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez envoyé une demande d'ami à " + EndoSkullAPI.getPrefix(targetUUID) + targetName + "\n" + EndoSkullAPI.LINE));
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            if (!FriendUtils.areFriends(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'êtes pas amis avec ce joueur\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            FriendUtils.removeFriend(player.getUniqueId(), targetUUID);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §cvient de vous retirer de sa liste d'amis\n" + EndoSkullAPI.LINE));
            }
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'est dorénavant plus amis avec " + EndoSkullAPI.getPrefix(targetUUID) + targetName + "\n" + EndoSkullAPI.LINE));
            return;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("accept")) {
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'existe pas\n" +
                        EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            if (!FriendUtils.hasRequestFrom(player.getUniqueId(), targetUUID)) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous n'avez pas de demande d'ami de la part de ce joueur\n" + EndoSkullAPI.LINE).toLegacyText());
                return;
            }
            FriendUtils.removeRequest(targetUUID, player.getUniqueId());
            FriendUtils.addFriend(targetUUID, player.getUniqueId());
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous êtes maintenant ami avec " + EndoSkullAPI.getPrefix(targetUUID) + targetName + "\n" + EndoSkullAPI.LINE).toLegacyText());
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n" + EndoSkullAPI.getPrefix(player.getUniqueId()) + player.getName() + " §avient d'accepter votre demande d'ami\n" + EndoSkullAPI.LINE).toLegacyText());
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

        player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aCommandes d'amis:\n" +
                "§e/friend accept (Pseudo) §8- §7Accepter une demande d'ami\n" +
                "§e/friend add (Pseudo) §8- §7Ajouter un joueur en ami\n" +
                "§e/friend list [Page] §8- §7Afficher votre liste d'amis\n" +
                "§e/friend settings §8- §7Ouvrir les paramètres liés aux amis\n" +
                "§e/friend requests §8- §7Afficher la liste des demandes d'amis\n" +
                EndoSkullAPI.LINE).toLegacyText());
    }
}
