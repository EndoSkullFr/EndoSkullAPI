package fr.endoskull.api.bungee.commands;

import com.google.common.base.Joiner;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

public class OmgCommand extends Command {
    public OmgCommand() {
        super("omg", "force.save");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            if (args.length < 2) return;
            String username = args[0];
            String purchase = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage(BungeeLang.getLang(player).getMessage(MessageUtils.Global.SHOP_BROADCAST).replace("{player}", username).replace("{purchase}", purchase));
            }

            return;
        }
    }

}
