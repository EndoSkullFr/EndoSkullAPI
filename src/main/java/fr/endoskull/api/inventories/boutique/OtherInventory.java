package fr.endoskull.api.inventories.boutique;

import fr.endoskull.api.utils.CustomGui;
import fr.endoskull.api.utils.CustomItemStack;
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
        setItem(12, new CustomItemStack(Material.GOLD_NUGGET).setName("§eBooster de Coins x1.5").setLore("\n§7⇨ Prix: §e1.5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(20, new CustomItemStack(Material.GOLD_INGOT).setName("§eBooster de Coins x2").setLore("\n§7⇨ Prix: §e2.5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(30, new CustomItemStack(Material.GOLD_BLOCK).setName("§eBooster de Coins x5").setLore("\n§7⇨ Prix: §e8.5€"), player -> BoutiqueInventory.sendStoreLink(player));

        setItem(14, new CustomItemStack(Material.TRIPWIRE_HOOK).setName("§41 Clé Ultime").setLore("\n§7⇨ Prix: §42€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(24, new CustomItemStack(Material.TRIPWIRE_HOOK, 5).setName("§45 Clé Ultime").setLore("\n§7⇨ Prix: §48.5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(32, new CustomItemStack(Material.TRIPWIRE_HOOK, 10).setName("§410 Clé Ultime").setLore("\n§7⇨ Prix: §415€"), player -> BoutiqueInventory.sendStoreLink(player));

        setItem(40, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory().open(player);
        });
    }
}
