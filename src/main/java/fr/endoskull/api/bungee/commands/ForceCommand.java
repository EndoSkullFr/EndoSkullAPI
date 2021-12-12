package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.data.redis.JedisAccess;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForceCommand extends Command {
    private BungeeMain main;
    private static List<UUID> waitingPlayer = new ArrayList<>();
    public ForceCommand(BungeeMain main) {
        super("forcesave", "force.save");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cEnvoies vers les bases de données en cours...");
            JedisAccess.sendToDatabase();
            return;
        }
    }

}
