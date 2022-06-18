package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.commons.lang.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
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
                sender.sendMessage("§cEndoSkull §8» §c/key add/set/remove {player} {clé} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = SpigotPlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.UNKNOWN_PLAYER));
                return;
            }
            if (!JedisManager.isLoad(targetUUID)) AccountProvider.loadAccount(targetUUID);
            Account account = new AccountProvider(targetUUID).getAccount();

            String key = args[2].toLowerCase();
            int number = 0;
            try {
                number = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {
                sender.sendMessage("§cEndoSkull §8» §cLe quatrième argument n'est pas un nombre !");
                return;
            }
            if (key.equalsIgnoreCase("vote") || key.equalsIgnoreCase("ultime")) {
                if (args[0].equals("add")) {
                    account.incrementStatistic("key/" + key, number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.incrementStatistic("key/" + key, number * -1);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setStatistic("key/" + key, number);
                    sender.sendMessage("§a" + number + " §eClés §a" + key + " §ea/ont été défini à §a" + args[1]);
                }
            }
        });
        return false;
    }
}