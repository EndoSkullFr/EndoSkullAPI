package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.bungee.utils.BungeePlayerInfos;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.data.redis.JedisManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class KeyCommand extends Command {
    public KeyCommand() {
        super("key", "endoskull.key.edit");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getInstance(), () -> {
            if (args.length < 4) {
                sender.sendMessage("§cEndoSkull §8» §c/key add/set/remove {player} {clé} {number}");
                return;
            }
            String targetName = args[1];
            UUID targetUUID = BungeePlayerInfos.getUuidFromName(targetName);
            if (targetUUID == null) {
                sender.sendMessage(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.UNKNOWN_PLAYER));
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
    }
}