package fr.endoskull.api.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OmgCommand extends Command {
    public OmgCommand() {
        super("omg", "force.save");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            if (args.length < 2) return;
            String username = args[0];
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length ; i++) {
                if (builder.length() > 0) builder.append(" ");
                builder.append(args[i]);
            }
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                player.sendMessage("§aUn maximum de remerciements pour §c" + username + " §apour son achat sur la boutique ! §7(" + builder + ")");
            }

            return;
        }
    }

}
