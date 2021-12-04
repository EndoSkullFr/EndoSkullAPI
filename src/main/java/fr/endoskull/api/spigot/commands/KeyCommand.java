package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.Account;
import fr.endoskull.api.commons.AccountProvider;
import fr.endoskull.api.spigot.utils.PlayerInfos;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class KeyCommand implements CommandExecutor {
    private Main main;
    public KeyCommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 4) {
                sender.sendMessage("/key add/set/remove {player} {clé} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = PlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage("§4Ce joueur n'existe pas !");
                return;
            }
            Account account = new AccountProvider(targetUUID).getAccount();

            String key = args[2].toLowerCase();
            int number = 0;
            try {
                number = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§4Le quatrième argument n'est pas un nombre !");
                return;
            }
            if (key.equalsIgnoreCase("ultime")) {
                if (args[0].equals("add")) {
                    account.setUltimeKey(account.getUltimeKey() + number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setUltimeKey(account.getUltimeKey() - number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setUltimeKey(number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            if (key.equalsIgnoreCase("vote")) {
                if (args[0].equals("add")) {
                    account.setVoteKey(account.getVoteKey() + number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setVoteKey(account.getVoteKey() - number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setVoteKey(number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            if (key.equalsIgnoreCase("coins")) {
                if (args[0].equals("add")) {
                    account.setCoinsKey(account.getCoinsKey() + number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setCoinsKey(account.getCoinsKey() - number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setCoinsKey(number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
            if (key.equalsIgnoreCase("kit")) {
                if (args[0].equals("add")) {
                    account.setKitKey(account.getKitKey() + number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setKitKey(account.getKitKey() - number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setKitKey(number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
        });
        return false;
    }
}
