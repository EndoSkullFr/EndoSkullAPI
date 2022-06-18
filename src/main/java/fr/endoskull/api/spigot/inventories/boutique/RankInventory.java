package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;

public class RankInventory extends CustomGui {
    public RankInventory(Player p) {
        super(5, Languages.getLang(p).getMessage(MessageUtils.Global.GUI_RANKS), p);
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(2).setName("§r"));
        }
        setItem(20, new CustomItemStack(Material.GOLD_CHESTPLATE).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_VIP)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(21, new CustomItemStack(Material.NAME_TAG).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_FEATURES)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_VIP_DESC)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(23, new CustomItemStack(Material.DIAMOND_CHESTPLATE).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_HERO)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(24, new CustomItemStack(Material.NAME_TAG).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_FEATURES)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_HERO_DESC)), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(40, new CustomItemStack(Material.ARROW).setName(Languages.getLang(p).getMessage(MessageUtils.Global.BACK)), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory(player).open();
        });
    }
}
