package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.utils.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BoosterCommand implements CommandExecutor {
    private Main main;
    public BoosterCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("booster.edit") || args.length == 0) {
                Account account;
                account = new AccountProvider(player.getUniqueId()).getAccount();
                player.sendMessage("§7§m--------------------\n" +
                        "§eBooster: §6x" + account.getBooster() + "\n" +
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
            Account account = new AccountProvider(targetUUID).getAccount();

            double number = 0;
            try {
                number = Double.parseDouble(args[2]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                return;
            }
            if (args[0].equals("add")) {
                account.setBooster(account.getBooster() + number).sendToRedis();
                sender.sendMessage("§a" + number + " §ebooster ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("remove")) {
                account.setBooster(account.getBooster() - number).sendToRedis();
                sender.sendMessage("§a" + number + " §eboster ont été retiré à §a" + args[1]);
            }
            if (args[0].equals("set")) {
                account.setBooster(number).sendToRedis();
                sender.sendMessage("§a" + number + " §ebooster ont été défini à §a" + args[1]);
            }
        });
        return false;
    }
}
