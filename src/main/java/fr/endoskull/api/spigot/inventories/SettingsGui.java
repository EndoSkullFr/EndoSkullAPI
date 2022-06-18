package fr.endoskull.api.spigot.inventories;

import fr.endoskull.api.commons.paf.FriendSettingsSpigot;
import fr.endoskull.api.commons.paf.FriendUtils;
import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.spigot.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SettingsGui extends CustomGui {
    public SettingsGui(Player p) {
        super(4, "§cEndoSkull §8» §eParamètres", p);
        Languages lang = Languages.getLang(p);
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1f, 1f);

        for (FriendSettingsSpigot value : FriendSettingsSpigot.values()) {
            boolean b = FriendUtils.getSetting(p.getUniqueId(), value).equalsIgnoreCase("1");
            setItem(value.getSlot(), new CustomItemStack(value.getItem()).setName("§e" + lang.getMessage(value.getName())));
            setItem(value.getSlot() + 9, new CustomItemStack(Material.INK_SACK).setData((byte) (b ? 10 : 1)).setName(lang.getMessage(MessageUtils.Global.CLICK_FOR) + (b ? lang.getMessage(MessageUtils.Global.DISABLE) : lang.getMessage(MessageUtils.Global.ENABLE))), player -> {
                FriendUtils.setSetting(player.getUniqueId(), value, (b ? "0" : "1"));
                player.sendMessage(lang.getMessage(MessageUtils.Global.FRIENDS) + (b ? lang.getMessage(value.getDisable()) : lang.getMessage(value.getEnable())));
                new SettingsGui(player).open(player);
            });
        }
    }
}
