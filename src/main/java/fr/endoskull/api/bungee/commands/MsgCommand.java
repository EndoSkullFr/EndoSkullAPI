package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MsgCommand extends Command {
    public MsgCommand() {
        super("msg", "", "message", "pm", "tell");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length >= 2) {
            String targetName = args[0];
            if (targetName.equalsIgnoreCase(player.getName())) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cVous ne pouvez pas vous envoyer de message\n" +
                        EndoSkullAPI.LINE));
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if (target == null) {
                player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur n'est pas connecter\n" +
                        EndoSkullAPI.LINE));
                return;
            }

            if (!FriendUtils.getSetting(target.getUniqueId(), FriendSettingsBungee.PRIVATE_MESSAGE).equalsIgnoreCase("1")) {
                if (!FriendUtils.areFriends(player.getUniqueId(), target.getUniqueId())) {
                    player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§cCe joueur a desactivé les messages privés ne provenant pas de ses amis\n" + EndoSkullAPI.LINE));
                    return;
                }
            }
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                messageBuilder.append(args[i]).append(" ");
            }
            String message = messageBuilder.toString();
            message = message.substring(0, message.length() - 1);
            player.sendMessage(new TextComponent("§a§lMSG §8» " + EndoSkullAPI.getColor(player.getUniqueId()) + player.getName() + " §7➼ " + EndoSkullAPI.getColor(target.getUniqueId()) + target.getName() + " §7» §f" + message));
            target.sendMessage(new TextComponent("§a§lMSG §8» " + EndoSkullAPI.getColor(player.getUniqueId()) + player.getName() + " §7➼ " + EndoSkullAPI.getColor(target.getUniqueId()) + target.getName() + " §7» §f" + message));
            BungeeMain.getInstance().getLastPM().put(player, target);
            BungeeMain.getInstance().getLastPM().put(target, player);

            return;
        }
        player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aUtilisation:\n" +
                "§e/msg (Pseudo) (Message)\n" +
                EndoSkullAPI.LINE));

    }
}
