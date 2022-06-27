package fr.endoskull.api.spigot.commands;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import fr.endoskull.api.Main;
import fr.endoskull.api.commons.paf.Party;
import fr.endoskull.api.commons.paf.PartyUtils;
import fr.endoskull.api.commons.server.ServerManager;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.spigot.inventories.ServerGui;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private Main main;
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry()
            .getFirstService(IPlayerManager.class);

    public JoinCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.CONSOLE));
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("§cEndoSkull §c» §c/join [serveur] {gui}");
            return false;
        }
        ServerType serverType = ServerType.getByName(args[0]);
        if (serverType == null) {
            player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.UNKNOWN_SERVER));
            return false;
        }
        if (!player.hasPermission("endoskull.join." + serverType.toString().toLowerCase())) {
            player.sendMessage(Languages.getLang(player).getMessage(MessageUtils.Global.SERVER_MAINTENANCE));
            return false;
        }
        if (!serverType.isMultiArena() && args.length > 1 && args[1].equalsIgnoreCase("gui")) {
            new ServerGui(serverType, main, player).open(player);
            return false;
        }
        int partySize = 1;
        if (PartyUtils.isInParty(player.getUniqueId())) {
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (party.getLeader().equals(player.getUniqueId())) {
                partySize = party.getPlayers().size();
            }
        }
        String server = ServerManager.getServer(serverType, partySize);
        if (server == null) {
            player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.ANY_SERVER));
            return false;
        }
        playerManager.getPlayerExecutor(player.getUniqueId()).connect(server);
        return false;
    }
}
