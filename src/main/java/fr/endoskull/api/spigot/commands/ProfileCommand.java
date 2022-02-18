package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.commons.server.ServerManager;
import fr.endoskull.api.commons.server.ServerType;
import fr.endoskull.api.spigot.inventories.ProfileInventory;
import fr.endoskull.api.spigot.inventories.ServerInventory;
import fr.endoskull.api.spigot.utils.PartyInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return false;
        }
        Player player = (Player) sender;
        new ProfileInventory(player).open(player);
        return false;
    }
}
