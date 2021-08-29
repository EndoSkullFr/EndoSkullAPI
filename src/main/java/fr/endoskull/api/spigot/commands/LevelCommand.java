package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.commons.exceptions.AccountNotFoundException;
import fr.endoskull.api.data.sql.Level;
import fr.endoskull.api.spigot.utils.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LevelCommand implements CommandExecutor {
    private Main main;
    public LevelCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("level.edit") || args.length == 0) {
                Account account;
                try {
                    account = new AccountProvider(player.getUniqueId()).getAccount();
                } catch (AccountNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                player.sendMessage("§7§m--------------------\n" +
                        "§eLevel : §6" + account.getLevel() + " §f| §eXp : §6" + account.getXp() + "§e/§6" + account.xpToLevelSup() + "\n" +
                        "§7§m--------------------");
                return false;
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 3) {
                sender.sendMessage("/" + label + " add/set/remove {player} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = PlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }

            double number = 0;
            try {
                number = Integer.parseInt(args[2]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                return;
            }
            if (args[0].equals("add")) {
                //Level.addXp(targetUUID, number);
                sender.sendMessage("§a" + number + " §exp ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("remove")) {
                //Level.removeXp(targetUUID, number);
                sender.sendMessage("§a" + number + " §exp ont été retiré à §a" + args[1]);
            }
            if (args[0].equals("set")) {
                //Level.setXp(targetUUID, number);
                sender.sendMessage("§a" + number + " §exp ont été défini à §a" + args[1]);
            }
        });
        return false;
    }
}
