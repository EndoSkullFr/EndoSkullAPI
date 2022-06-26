package fr.endoskull.api.bungee.commands;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class VoteCommand extends Command {
    public VoteCommand() {
        super("vote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BungeeLang lang = BungeeLang.getLang(sender);
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(lang.getMessage(MessageUtils.Global.CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 0) {
            TextComponent msg = new TextComponent(TextComponent.fromLegacyText(lang.getMessage(MessageUtils.Global.VOTE)));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(lang.getMessage(MessageUtils.Global.CLICK_HOVER)).create()));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://endoskull.net/vote"));
            player.sendMessage(msg);
            return;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("claim")) {
            ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getInstance(), () -> {
                String url = "https://serveur-prive.net/api/vote/json/O6EAaRCpSJTlL2X/" + player.getAddress().getHostName();
                try {
                    String json = IOUtils.toString(new URL(url));
                    if(json.isEmpty()) {
                        player.sendMessage(lang.getMessage(MessageUtils.Global.VOTE_ERROR));
                        return;
                    }
                    JSONObject object = (JSONObject) JSONValue.parseWithException(json);

                    if (!((String) object.get("status")).equalsIgnoreCase("1")) {
                        player.sendMessage(lang.getMessage(MessageUtils.Global.NO_VOTE));
                        return;
                    }
                    String vote = (String) object.get("vote");
                    Account account = AccountProvider.getAccount(player.getUniqueId());
                    if (vote.equalsIgnoreCase(account.getProperty("lastVote", "0"))) {
                        player.sendMessage(lang.getMessage(MessageUtils.Global.VOTE_ALREADY));
                        return;
                    }
                    ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), "key add " + player.getName() + " vote 1");
                    account.setProperty("lastVote", vote);
                    player.sendMessage(lang.getMessage(MessageUtils.Global.VOTE_CLAIM));

                } catch (IOException | ParseException | NullPointerException e) {
                    player.sendMessage(lang.getMessage(MessageUtils.Global.VOTE_ERROR));
                }
            });
            return;
        }
    }
}
