package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
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
            sender.sendMessage(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Account account = AccountProvider.getAccount(player.getUniqueId());

        if (args.length >= 1 && args[0].equalsIgnoreCase("new")) {
            String token = generateToken();
            account.setProperty("api/token", token);
            player.sendMessage(new TextComponent(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.API_ALREADY).replace("{token}", token)).toLegacyText());
            TextComponent textComponent = new TextComponent(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.CLICK_COPY));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, token));
            player.sendMessage(textComponent);
            return;
        }
        if (account.getProperty("api/token").equalsIgnoreCase("")) {
            player.sendMessage(new TextComponent(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.API_HELP)).toLegacyText());
        } else {
            player.sendMessage(new TextComponent(BungeeLang.getLang(sender).getMessage(MessageUtils.Global.API_ALREADY)).toLegacyText());
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
