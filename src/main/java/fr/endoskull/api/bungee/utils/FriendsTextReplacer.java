package fr.endoskull.api.bungee.utils;

import de.simonsator.partyandfriends.api.TextReplacer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayer;

public class FriendsTextReplacer implements TextReplacer {
    @Override
    public String onProecess(PAFPlayer pafPlayer, String s) {
        return s.replace(pafPlayer.getDisplayName(), "§7[" + (pafPlayer.isOnline() ? "§a" : "§c") + "✦§7] §7- " + pafPlayer.getDisplayName());
    }
}
