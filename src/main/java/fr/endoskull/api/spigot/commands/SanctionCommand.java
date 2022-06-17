package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.spigot.inventories.SanctionGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SanctionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("§cEndoSkull §8» §c/" + label + " {Pseudo}");
            return false;
        }
        String target = args[0];
        new SanctionGui(target).open(player);
        return false;
    }
}
