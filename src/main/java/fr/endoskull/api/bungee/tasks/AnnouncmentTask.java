package fr.endoskull.api.bungee.tasks;

import fr.endoskull.api.bungee.utils.BungeeLang;
import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.text.TextUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AnnouncmentTask implements Runnable {
    private MessageUtils[] messages = {MessageUtils.Global.ANNOUNCEMENT_1, MessageUtils.Global.ANNOUNCEMENT_2, MessageUtils.Global.ANNOUNCEMENT_3};
    private int index = 0;

    @Override
    public void run() {
        if (messages.length <= index) index = 0;
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            BungeeLang lang = BungeeLang.getLang(player);
            player.sendMessage(new TextComponent("§r\n" + TextUtil.getCenteredMessage(lang.getMessage(MessageUtils.Global.ANNOUNCEMENT) + "\n" + TextUtil.getCenteredMessage(lang.getMessage(messages[index])) + "\n§r")).toLegacyText());
        }
        BungeeLang lang = BungeeLang.getLang(ProxyServer.getInstance().getConsole());
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§r\n" + TextUtil.getCenteredMessage(lang.getMessage(MessageUtils.Global.ANNOUNCEMENT)) + "\n" + TextUtil.getCenteredMessage(lang.getMessage(messages[index])) + "\n§r").toLegacyText());
        index++;
    }
}
