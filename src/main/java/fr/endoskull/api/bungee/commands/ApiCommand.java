package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.EndoSkullAPI;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Random;
import java.util.UUID;

public class ApiCommand extends Command {
    public ApiCommand() {
        super("api");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Account account = AccountProvider.getAccount(player.getUniqueId());

        if (args.length >= 1 && args[0].equalsIgnoreCase("new")) {
            String token = generateToken();
            account.setProperty("api/token", token);
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§7Votre token est §8» §e" + token + "\n§7Pour consulter la documentation §8» §ehttps://api.endoskull.fr\n" + EndoSkullAPI.LINE).toLegacyText());
            TextComponent textComponent = new TextComponent("§7(Cliquez pour copier)");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, token));
            player.sendMessage(textComponent);
            return;
        }
        if (account.getProperty("api/token").equalsIgnoreCase("")) {
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aSi vous souhaitez générer un token pour pouvoir utiliser notre api faîtes §b/api new\n§eSi vous souhaitez consulter la documentation rdv sur https://api.endoskull.fr\n" + EndoSkullAPI.LINE).toLegacyText());
        } else {
            player.sendMessage(new TextComponent(EndoSkullAPI.LINE + "\n§aVous avez déjà un token de générer, si vous souhaitez consulter la documentation rdv sur https://api.endoskull.fr\n§eSi vous souhaitez regénérer votre token faîtes §b/api new\n" + EndoSkullAPI.LINE).toLegacyText());
        }

    }

    private static String generateToken() {
        /*String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(30);
        for (int i = 0; i < 30; i++)
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        return sb.toString();*/
        return UUID.randomUUID().toString();
    }
}
