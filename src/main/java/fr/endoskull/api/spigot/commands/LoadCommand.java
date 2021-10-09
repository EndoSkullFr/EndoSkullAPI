package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.AccountProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LoadCommand implements CommandExecutor {
    private Main main;
    public LoadCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.getName().equalsIgnoreCase("CONSOLE") && !(sender instanceof Player)) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                for (UUID uuid : main.getMySQL().getAllUniqueId()) {
                    new AccountProvider(uuid).getAccount();
                }
                sender.sendMessage("§aTous les comptes viennent d'être chargé");
            });
        }
        return false;
    }
}
