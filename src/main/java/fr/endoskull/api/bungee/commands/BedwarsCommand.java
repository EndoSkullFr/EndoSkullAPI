package fr.endoskull.api.bungee.commands;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import fr.bebedlastreat.cache.data.redis.RedisManager;
import fr.endoskull.api.BungeeMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BedwarsCommand extends Command {
    private BungeeMain main;
    private static List<UUID> waitingPlayer = new ArrayList<>();
    public BedwarsCommand(BungeeMain main) {
        super("bedwars", "admin");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;


        player.sendMessage("§cVous êtes en file d'attente pour le bedwars");
        main.getWaitingServer().put(player.getUniqueId(), "BedwarsSolo");
    }

}
