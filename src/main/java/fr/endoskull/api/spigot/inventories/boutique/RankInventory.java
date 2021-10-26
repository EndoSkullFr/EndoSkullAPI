package fr.endoskull.api.spigot.inventories.boutique;

import fr.endoskull.api.spigot.utils.CustomGui;
import fr.endoskull.api.spigot.utils.CustomItemStack;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;

public class RankInventory extends CustomGui {
    public RankInventory() {
        super(5, "§5Boutique EndoSkull");
        int[] glassSlots = {0, 1, 7, 8, 9, 17, 27, 35, 36, 37, 43, 44};
        for (int i : glassSlots) {
            setItem(i, CustomItemStack.getPane(2).setName("§r"));
        }
        setItem(21, new CustomItemStack(Material.GOLD_SWORD).setName("§eGrade Officier").setLore("\n§7Liste de ces fonctionnalités:\n§7◇ Un booster de coins x1.5\n§7◇ Le préfix §eOfficier §7dans la tablist\n§7◇ Votre pseudo en §ejaune §7dans le chat\n§7◇ Permission de fly au lobby\n§7◇ Accès au /pp pour avoir des particules\n§7◇ Accès aux kits de Tier 2 sur le PvpKit\n\n§7⇨ Prix: §e5€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(23, new CustomItemStack(Material.DIAMOND_SWORD).setName("§cGrade Général").setLore("\n§7Liste de ces fonctionnalités:\n§7◇ Avantages du grade précédent\n§7◇ Un booster de coins x2\n§7◇ Le préfix §cGénéral §7dans la tablist\n§7◇ Votre pseudo en §crouge §7dans le chat\n§7◇ Accès au /tag pour créer un tag personnaliser\n§7◇ Accès au /pets pour invoquer une monture\n§7◇ Accès aux kits de Tier 3 sur le PvpKit\n\n§7⇨ Prix: §c10€"), player -> BoutiqueInventory.sendStoreLink(player));
        setItem(40, new CustomItemStack(Material.ARROW).setName("§eRetour"), player -> {
            player.playNote(player.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.A));
            new BoutiqueInventory().open(player);
        });
    }
}
