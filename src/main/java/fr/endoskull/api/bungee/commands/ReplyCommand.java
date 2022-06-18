package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.FriendSettingsBungee;
import fr.endoskull.api.commons.paf.FriendUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {
    public ReplyCommand() {
        super("reply", "", "r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BungeeLang lang = BungeeLang.getLang(sender);
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length >= 1) {
            ProxiedPlayer target = BungeeMain.getInstance().getLastPM().get(player);
            if (target == null) {
                player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.REPLY_ANY)).toLegacyText());
                return;
            }
            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                messageBuilder.append(args[i]).append(" ");
            }
            String message = messageBuilder.toString();
            message = message.substring(0, message.length() - 1);
            ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "msg " + target.getName() + " " + message);
            return;
        }
        player.sendMessage(new TextComponent(lang.getMessage(MessageUtils.Paf.REPLY_USAGE)).toLegacyText());

    }
}
