package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;

public class OtherInventory extends CustomGui {
    public OtherInventory(Player p) {
        super(5, Languages.getLang(p).getMessage(MessageUtils.Global.GUI_KEYS), p);
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(2).setName("§r"));
        }

        setItem(21, new CustomItemStack(Material.ENDER_CHEST).setName(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_1)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_1_DESC)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(22, new CustomItemStack(Material.ENDER_CHEST, 5).setName(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_5)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_5_DESC)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(23, new CustomItemStack(Material.ENDER_CHEST, 10).setName(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_10)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.ULTIME_10_DESC)), player -> BoutiqueInventory.sendStoreLink(player));

        setItem(40, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory(player).open();
        });
    }
}
