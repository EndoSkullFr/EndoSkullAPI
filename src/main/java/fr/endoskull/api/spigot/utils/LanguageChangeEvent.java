package fr.endoskull.api.spigot.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LanguageChangeEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Languages oldLang;
    private Languages newLang;
    private Player player;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public Languages getOldLang() {
        return oldLang;
    }

    public Languages getNewLang() {
        return newLang;
    }

    public Player getPlayer() {
        return player;
    }

    public LanguageChangeEvent(Languages oldLang, Languages newLang, Player player) {
        this.oldLang = oldLang;
        this.newLang = newLang;
        this.player = player;
    }
}
