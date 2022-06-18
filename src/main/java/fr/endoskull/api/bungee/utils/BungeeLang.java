package fr.endoskull.api.bungee.utils;

import fr.endoskull.api.BungeeMain;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum BungeeLang {
    FRENCH(),
    ENGLISH();

    public String getMessage(MessageUtils messageUtils) {
        if (BungeeMain.getLangFiles().get(this).get(messageUtils.getPath()) == null) {
            if (this == BungeeLang.FRENCH) {
                throw new IllegalStateException(messageUtils.getPath() + " not exist in French file");
            }
            return BungeeLang.FRENCH.getMessage(messageUtils);
        }
        return BungeeMain.getLangFiles().get(this).getString(messageUtils.getPath()).replace("{line}", EndoSkullAPI.LINE);
    }

    public static BungeeLang getLang(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            if (BungeeMain.getLangs().containsKey(player)) {
                return BungeeMain.getLangs().get(player);
            }
            return FRENCH;
        }
        return ENGLISH;
    }
}
