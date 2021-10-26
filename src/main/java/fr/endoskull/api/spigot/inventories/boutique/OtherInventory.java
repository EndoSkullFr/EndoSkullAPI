package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;

public class OtherInventory extends CustomGui {
    public OtherInventory() {
        super(5, "§5Boutique EndoSkull");
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(2).setName("§r"));
        }

        setItem(21, new CustomItemStack(Material.TRIPWIRE_HOOK).setName("§41 Clé Ultime").setLore("\n§7⇨ Prix: §42€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(22, new CustomItemStack(Material.TRIPWIRE_HOOK, 5).setName("§45 Clé Ultime").setLore("\n§7⇨ Prix: §48.5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(23, new CustomItemStack(Material.TRIPWIRE_HOOK, 10).setName("§410 Clé Ultime").setLore("\n§7⇨ Prix: §415€"), player -> BoutiqueInventory.sendStoreLink(player));

        setItem(40, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory().open(player);
        });
    }
}
