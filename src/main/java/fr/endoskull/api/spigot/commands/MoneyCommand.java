package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MoneyCommand implements CommandExecutor {
    private Main main;
    public MoneyCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("coins.edit") || args.length == 0) {
                Account account;
                account = new AccountProvider(player.getUniqueId()).getAccount();
                player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.COINS).replace("{coins}", String.valueOf(account.getSolde())));
                return false;
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 3) {
                sender.sendMessage("/" + label + " add/set/remove/give {player} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = SpigotPlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }
            if (!JedisManager.isLoad(targetUUID)) AccountProvider.loadAccount(targetUUID);
            Account account = new AccountProvider(targetUUID).getAccount();

            double number = 0;
            try {
                number = Double.parseDouble(args[2]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                return;
            }
            if (args[0].equals("add")) {
                account.addMoney(number);
                sender.sendMessage("§a" + number + " §ecoins ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("give")) {
                account.addMoneyWithBooster(number);
                sender.sendMessage("§a" + number + " §ecoins ont été ajouté à §a" + args[1]);
            }
            if (args[0].equals("remove")) {
                account.removeMoney(number);
                sender.sendMessage("§a" + number + " §ecoins ont été retiré à §a" + args[1]);
            }
            if (args[0].equals("set")) {
                account.setSolde(number);
                sender.sendMessage("§a" + number + " §ecoins ont été défini à §a" + args[1]);
            }
        });
        return false;
    }
}
