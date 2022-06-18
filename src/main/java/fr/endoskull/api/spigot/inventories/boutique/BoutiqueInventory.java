package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.api.commons.lang.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class BoutiqueInventory extends CustomGui {
    public BoutiqueInventory(Player p) {
        super(5, Languages.getLang(p).getMessage(MessageUtils.Global.GUI_SHOP), p);
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(5).setName("§r"));
        }
        setItem(21, new CustomItemStack(Material.ENCHANTED_BOOK).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_RANKS)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_RANKS_DESC)), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new RankInventory(player).open();
        });
        setItem(23, new CustomItemStack(Material.TRIPWIRE_HOOK).setName(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_KEYS)).setLore(Languages.getLang(p).getMessage(MessageUtils.Global.SHOP_KEYS_DESC)), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new OtherInventory(player).open();
        });
    }

    public static void sendStoreLink(Player player) {
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0f, 0.5f);
        TextComponent msg = new TextComponent(TextComponent.fromLegacyText(Languages.getLang(player).getMessage(MessageUtils.Global.SHOP_BOUGHT)));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Languages.getLang(player).getMessage(MessageUtils.Global.CLICK_HOVER)).create()));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.endoskull.fr"));
        player.spigot().sendMessage(msg);
    }
}
