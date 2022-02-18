package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class BoutiqueInventory extends CustomGui {
    public BoutiqueInventory() {
        super(5, "§c§lEndoSkull §8» §a§lBoutique");
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(5).setName("§r"));
        }
        setItem(21, new CustomItemStack(Material.ENCHANTED_BOOK).setName("§aGrades").setLore("\n§7Dans cette catégorie, vous trouverez:\n§7► Le grade §eOfficer\n§7► Le grade §cGénéral"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new RankInventory().open(player);
        });
        setItem(23, new CustomItemStack(Material.TRIPWIRE_HOOK).setName("§aClés").setLore("\n§7Dans cette catégorie, vous trouverez:\n§7► Des Clé Ultimes"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new OtherInventory().open(player);
        });
    }

    public static void sendStoreLink(Player player) {
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0f, 0.5f);
        TextComponent msg = new TextComponent(TextComponent.fromLegacyText("§7§m--------------------------------------------------\n" +
                ChatColor.YELLOW + "Pour acheter cette article, rendez-vous sur la boutique en ligne: " + ChatColor.GREEN + "store.endoskull.fr\n" +
                "§7§m--------------------------------------------------"));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fCLIQUEZ POUR OUVRIR").create()));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://store.endoskull.fr"));
        player.spigot().sendMessage(msg);
    }
}
