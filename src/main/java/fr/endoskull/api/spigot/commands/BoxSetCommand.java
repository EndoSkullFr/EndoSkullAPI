package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoxSetCommand implements CommandExecutor {
    private Main main;
    public BoxSetCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.isOp()) return false;

        if (main.getWaitingSetting().containsKey(player)) {
            player.sendMessage("§cVous avez déjà éxécuter cette commande auparavant sans avoir cliquer sur un block");
        } else {
            main.getWaitingSetting().put(player, null);
            player.sendMessage("§cCliquez maintenant sur un block");
        }
        return false;
    }
}
