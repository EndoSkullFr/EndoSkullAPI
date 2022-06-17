package fr.endoskull.api.spigot.utils;

import fr.endoskull.api.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Languages {
    FRENCH("http://textures.minecraft.net/texture/6903349fa45bdd87126d9cd3c6c0abba7dbd6f56fb8d78701873a1e7c8ee33cf"),
    ENGLISH("http://textures.minecraft.net/texture/c439d7f9c67f32dcbb86b7010b1e14b60de96776a35f61cee982660aacf5264b");

    private String skull;

    Languages(String skull) {
        this.skull = skull;
    }

    public String getSkull() {
        return skull;
    }

    public String getMessage(MessageUtils messageUtils) {
        if (Main.getLangFiles().get(this).get(messageUtils.getPath()) == null) {
            if (this == Languages.FRENCH) {
                throw new IllegalStateException(messageUtils.getPath() + " not exist in French file");
            }
            return Languages.FRENCH.getMessage(messageUtils);
        }
        return Main.getLangFiles().get(this).getString(messageUtils.getPath());
    }

    public static Languages getLang(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Main.getLangs().containsKey(player)) {
                return Main.getLangs().get(player);
            }
            return FRENCH;
        }
        return ENGLISH;
    }
}
