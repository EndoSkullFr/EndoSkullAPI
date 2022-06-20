package fr.endoskull.api.spigot.utils;

import fr.endoskull.api.Main;
import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Languages {
    FRENCH("http://textures.minecraft.net/texture/6903349fa45bdd87126d9cd3c6c0abba7dbd6f56fb8d78701873a1e7c8ee33cf", "Français", "", 21),
    ENGLISH("http://textures.minecraft.net/texture/c439d7f9c67f32dcbb86b7010b1e14b60de96776a35f61cee982660aacf5264b", "English", "\n§7Translations may not be 100% accuracy but\n§7we do our best to ensure that they are.", 23);

    private String skull;
    private String name;
    private String lore;
    private int slot;

    Languages(String skull, String name, String lore, int slot) {
        this.skull = skull;
        this.name = name;
        this.lore = lore;
        this.slot = slot;
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
        return Main.getLangFiles().get(this).getString(messageUtils.getPath()).replace("{line}", EndoSkullAPI.LINE);
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

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public int getSlot() {
        return slot;
    }
}
