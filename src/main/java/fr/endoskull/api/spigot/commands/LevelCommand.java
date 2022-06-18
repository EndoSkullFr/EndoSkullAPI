package fr.endoskull.api.spigot.commands;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.data.redis.JedisManager;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.MessageUtils;
import fr.endoskull.api.spigot.utils.SpigotPlayerInfos;
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
                Account account = new AccountProvider(player.getUniqueId()).getAccount();
                player.sendMessage(Languages.getLang(sender).getMessage(MessageUtils.Global.LEVEL).replace("{level}", String.valueOf(account.getLevel()))
                        .replace("{xp}", String.valueOf(account.getXp())).replace("{levelup}", String.valueOf(account.xpToLevelSup())));
                return false;
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (args.length < 3) {
                sender.sendMessage("/" + label + " add/set/remove {player} {number}");
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
            if (label.equalsIgnoreCase("level")) {
                int number = 0;
                try {
                    number = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                    return;
                }
                if (args[0].equals("add")) {
                    account.setLevel(account.getLevel() + number);
                    sender.sendMessage("§a" + number + " §elevel ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setLevel(account.getLevel() - number);
                    sender.sendMessage("§a" + number + " §elevel ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setLevel(number);
                    sender.sendMessage("§a" + number + " §elevel ont été défini à §a" + args[1]);
                }
            } else {
                double number = 0;
                try {
                    number = Double.parseDouble(args[2]);

                } catch (NumberFormatException e) {
                    sender.sendMessage("§4Le troisième argument n'est pas un nombre !");
                    return;
                }
                if (number < 0) {
                    sender.sendMessage("§cLe nombre doit être supérieur à 0");
                    return;
                }
                if (number < 0) {
                    sender.sendMessage("§cLe nombre doit être supérieur à 0");
                    return;
                }
                if (args[0].equals("add")) {
                    account.setXp(account.getXp() + number);
                    sender.sendMessage("§a" + number + " §exp ont été ajouté à §a" + args[1]);
                }
                if (args[0].equals("remove")) {
                    account.setXp(account.getXp() - number);
                    sender.sendMessage("§a" + number + " §exp ont été retiré à §a" + args[1]);
                }
                if (args[0].equals("set")) {
                    account.setXp(number);
                    sender.sendMessage("§a" + number + " §exp ont été défini à §a" + args[1]);
                }
            }

        });
        return false;
    }
}
