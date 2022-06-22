package fr.endoskull.api.bungee.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerTeleportCommand extends Command {
    public ServerTeleportCommand() {
        super("serverteleport", "endoskull.stp", "stp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BungeeLang lang = BungeeLang.getLang(sender);
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length < 1) {
            player.sendMessage(lang.getMessage(MessageUtils.Global.STP_USAGE));
            return;
        }
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(lang.getMessage(MessageUtils.Global.OFFLINE_PLAYER));
            return;
        }
        Account account = AccountProvider.getAccount(player.getUniqueId());
        account.setProperty("serverTeleport", target.getName());
        Server server = target.getServer();
        if (server.getInfo().getName().equals(player.getServer().getInfo().getName())) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerTeleport");

            player.getServer().sendData(BungeeMain.CHANNEL, out.toByteArray());
            return;
        }
        player.connect(server.getInfo());


    }

    private static String generateToken() {
        /*String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(30);
        for (int i = 0; i < 30; i++)
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        return sb.toString();*/
        return UUID.randomUUID().toString();
    }
}
