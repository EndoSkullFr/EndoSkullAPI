package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnloadCommand extends Command {
    public UnloadCommand() {
        super("unload", "endoskull.admin.unload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§c/unload (Pseudo)");
            return;
        }
        String targetName = args[0];
        UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
        if (targetUUID == null) {
            sender.sendMessage("§cCe joueur n'existe pas");
            return;
        }
        if (JedisManager.isLoad(targetUUID)) {
            AccountProvider.unloadAccount(targetUUID);
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetUUID);
            if (target != null) {
                target.disconnect("§cVotre compte vient d'être déchargé par un administrateur");
            }
            sender.sendMessage("§aVous avec unload " + targetName);
        } else {
            sender.sendMessage("§cCe joueur n'est pas chargé");
        }

    }

}
