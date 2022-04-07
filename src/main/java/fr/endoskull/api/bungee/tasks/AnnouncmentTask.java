package fr.endoskull.api.bungee.tasks;

import fr.endoskull.api.commons.text.TextUtil;
import net.md_5.bungee.api.ProxyServer;

public class AnnouncmentTask implements Runnable {
    private String[] messages = {"§7Rejoignez le discord §e(discord.endoskull.fr)§7",
    "§7Supportez nous en achetant un grade sur la boutique §e(/boutique)§7",
    "§7Votez sur le serveur avec le §e/vote §7pour recevoir une clé vote"};
    private int index = 0;

    @Override
    public void run() {
        if (messages.length <= index) index = 0;
        ProxyServer.getInstance().broadcast("§r\n" + TextUtil.getCenteredMessage("§e§lANNONCE") + "\n" + TextUtil.getCenteredMessage(messages[index]) + "\n§r");
        index++;
    }
}
